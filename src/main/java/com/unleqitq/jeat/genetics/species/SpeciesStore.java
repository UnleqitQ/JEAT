package com.unleqitq.jeat.genetics.species;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.utils.tuple.Pair;
import com.unleqitq.jeat.utils.tuple.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class SpeciesStore {
	
	/**
	 * The species in the store
	 */
	@NotNull
	private final Map<UUID, Species> species = new HashMap<>();
	
	
	public SpeciesStore() {
	}
	
	/**
	 * Add a species to the store
	 * @param species The species to add
	 */
	public void add(@NotNull Species species) {
		this.species.put(species.id(), species);
	}
	
	/**
	 * Remove a species from the store
	 * @param species The species to remove
	 */
	public void remove(@NotNull Species species) {
		this.species.remove(species.id());
	}
	
	/**
	 * Remove a species from the store
	 * @param id The id of the species to remove
	 */
	public void remove(@NotNull UUID id) {
		Species species = this.species.remove(id);
	}
	
	/**
	 * Get a species from the store
	 * @param id The id of the species to get
	 * @return The species with the given id, or null if it doesn't exist
	 */
	@Nullable
	public Species get(@NotNull UUID id) {
		return species.get(id);
	}
	
	/**
	 * Get all species in the store
	 * @return A collection of all species in the store
	 */
	@NotNull
	public Collection<Species> species() {
		return species.values();
	}
	
	/**
	 * Get the number of species in the store
	 * @return The number of species in the store
	 */
	public int size() {
		return species.size();
	}
	
	/**
	 * Get the species with the highest fitness
	 * @return The species with the highest fitness
	 */
	@NotNull
	public Species best() {
		return Collections.max(species.values(), Comparator.comparing(Species::fitness));
	}
	
	/**
	 * Get the species with the highest fitness
	 * @param aggregationFunction The function to aggregate the fitness of all genomes
	 *                            if the fitness is not calculated
	 * @return The species with the highest fitness
	 */
	@NotNull
	public Species best(@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		return Collections.max(species.values(),
			Comparator.comparing(species -> species.fitness(aggregationFunction)));
	}
	
	/**
	 * Clear the store
	 */
	public void clear() {
		species.clear();
	}
	
	/**
	 * Clear the species in the store
	 */
	public void clearSpecies() {
		species.values().forEach(Species::clear);
	}
	
	/**
	 * Speciate a collection of genomes<br>
	 * Warning: This method will clear all remove all genomes from the species
	 *
	 * @param jeat The jeat instance
	 * @param genomes The genomes to speciate
	 */
	public void speciate(@NotNull Jeat jeat, @NotNull Collection<Genome> genomes) {
		// Creates a set to keep track of the genomes that have not yet been speciated
		Set<Genome> unspeciated = new HashSet<>(genomes);
		Set<UUID> discardedSpecies = new HashSet<>();
		class DistanceCache {
			
			private final Map<Pair<Genome, Genome>, Double> cache = new HashMap<>();
			
			public double get(Genome a, Genome b) {
				Pair<Genome, Genome> pair = Tuple.of(a, b);
				if (cache.containsKey(pair)) return cache.get(pair);
				double distance = a.distance(b);
				cache.put(pair, distance);
				cache.put(Tuple.of(b, a), distance);
				return distance;
			}
			
		}
		DistanceCache distanceCache = new DistanceCache();
		
		for (Species species : this.species()) {
			species.clear();
			
			// Get the representative genome of the species
			Genome rep = species.representative();
			
			// Find the genome closest to the representative
			Optional<Genome> closest =
				unspeciated.stream().min(Comparator.comparingDouble(g -> distanceCache.get(rep, g)));
			
			if (closest.isPresent()) {
				Genome g = closest.get();
				// Set the closest genome as the representative
				species.representative(g);
				// Add the closest genome to the species
				species.add(g);
				// Remove the genome from the unspeciated set
				unspeciated.remove(g);
			}
			else {
				// If no genome is found, discard the species
				discardedSpecies.add(species.id());
			}
		}
		
		// Remove species that didn't update their representative genome
		discardedSpecies.forEach(this::remove);
		
		// Get the compatibility threshold
		double compatibilityThreshold = jeat.config().species.compatibilityThreshold;
		// Speciate the remaining genomes
		for (Genome genome : unspeciated) {
			// Find the species with the closest representative genome
			Optional<Species> species = this.species()
				.stream()
				.filter(s -> distanceCache.get(s.representative(), genome) < compatibilityThreshold)
				.min(Comparator.comparingDouble(s -> distanceCache.get(s.representative(), genome)));
			
			if (species.isEmpty()) {
				// If no species is found, create a new one
				Species newSpecies = new Species(jeat, genome);
				this.add(newSpecies);
			}
			else {
				// Add the genome to the found species
				species.get().add(genome);
			}
		}
		
		// Remove species with no genomes
		Set.copyOf(this.species()).stream().filter(Species::isEmpty).forEach(this::remove);
		
		// Reset the fitness of all species and genomes
		this.species().stream().peek(Species::resetFitness).forEach(Species::resetGenomeFitnesses);
	}
	
}
