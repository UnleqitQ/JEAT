package com.unleqitq.jeat.population;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.genetics.species.Species;
import com.unleqitq.jeat.utils.tuple.Pair;
import com.unleqitq.jeat.utils.tuple.Tuple;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors (fluent = true)
public class Population {
	
	/**
	 * The Jeat instance that created this population.
	 */
	@NotNull
	@Getter
	private final Jeat jeat;
	
	/**
	 * The genomes in this population.
	 */
	@NotNull
	private final Map<UUID, Genome> genomes = new HashMap<>();
	
	/**
	 * The species in this population.
	 */
	@NotNull
	private final Map<UUID, Species> species = new HashMap<>();
	
	
	/**
	 * Creates a new population.
	 *
	 * @param jeat The Jeat instance that created this population.
	 */
	public Population(@NotNull Jeat jeat) {
		this.jeat = jeat;
	}
	
	/**
	 * Adds a genome to this population.
	 *
	 * @param genome The genome to add.
	 */
	public void add(@NotNull Genome genome) {
		this.genomes.put(genome.id(), genome);
	}
	
	/**
	 * Adds a species to this population.
	 *
	 * @param species The species to add.
	 */
	public void add(@NotNull Species species) {
		this.species.put(species.id(), species);
	}
	
	/**
	 * Gets a genome by its ID.
	 *
	 * @param id The ID of the genome to get.
	 * @return The genome with the given ID, or {@code null} if no such genome exists.
	 */
	public Genome getGenome(@NotNull UUID id) {
		return this.genomes.get(id);
	}
	
	/**
	 * Gets a species by its ID.
	 *
	 * @param id The ID of the species to get.
	 * @return The species with the given ID, or {@code null} if no such species exists.
	 */
	public Species getSpecies(@NotNull UUID id) {
		return this.species.get(id);
	}
	
	/**
	 * Removes a genome from this population. This is equivalent to calling {@link #removeGenome(Genome)}.
	 *
	 * @param genome The genome to remove.
	 */
	public void remove(@NotNull Genome genome) {
		this.removeGenome(genome);
	}
	
	/**
	 * Removes a species from this population. This is equivalent to calling {@link #removeSpecies(Species)}.
	 *
	 * @param species The species to remove.
	 */
	public void remove(@NotNull Species species) {
		this.species.remove(species.id());
	}
	
	/**
	 * Removes a genome from this population.
	 *
	 * @param id The ID of the genome to remove.
	 */
	public void removeGenome(@NotNull UUID id) {
		this.genomes.remove(id);
	}
	
	/**
	 * Removes a genome from this population.
	 *
	 * @param genome The genome to remove.
	 */
	public void removeGenome(@NotNull Genome genome) {
		this.genomes.remove(genome.id());
	}
	
	/**
	 * Removes a species from this population.
	 *
	 * @param id The ID of the species to remove.
	 */
	public void removeSpecies(@NotNull UUID id) {
		this.species.remove(id);
	}
	
	/**
	 * Removes a species from this population.
	 *
	 * @param species The species to remove.
	 */
	public void removeSpecies(@NotNull Species species) {
		this.species.remove(species.id());
	}
	
	/**
	 * Removes all genomes from this population.
	 */
	public void clearGenomes() {
		this.genomes.clear();
	}
	
	/**
	 * Removes all species from this population.
	 */
	public void clearSpecies() {
		this.species.clear();
	}
	
	/**
	 * Removes all genomes and species from this population.
	 */
	public void clear() {
		this.clearGenomes();
		this.clearSpecies();
	}
	
	/**
	 * Gets the number of genomes in this population.
	 *
	 * @return The number of genomes in this population.
	 */
	public int populationSize() {
		return this.genomes.size();
	}
	
	/**
	 * Gets the number of species in this population.
	 *
	 * @return The number of species in this population.
	 */
	public int speciesCount() {
		return this.species.size();
	}
	
	/**
	 * Clears all genomes from all species in this population.
	 */
	public void clearSpeciesGenomes() {
		this.species.values().forEach(Species::clear);
	}
	
	/**
	 * Creates a new genome and adds it to this population.
	 *
	 * @return The new genome.
	 */
	@NotNull
	public Genome createGenome() {
		Genome genome = new Genome(jeat);
		this.add(genome);
		return genome;
	}
	
	/**
	 * Creates a list of new genomes and adds them to this population.
	 *
	 * @param count The number of genomes to create.
	 * @return The new genomes.
	 */
	@NotNull
	public Collection<Genome> createGenomes(int count) {
		Collection<Genome> genomes = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			genomes.add(this.createGenome());
		}
		return genomes;
	}
	
	/**
	 * Creates a new species and adds it to this population.
	 *
	 * @param representative The genome that will represent the new species.
	 * @return The new species.
	 */
	@NotNull
	private Species createSpecies(Genome representative) {
		Species species = new Species(jeat, representative);
		this.add(species);
		return species;
	}
	
	/**
	 * Updates the species in this population.
	 */
	public void speciate() {
		
		// Creates a set to keep track of the genomes that have not yet been speciated
		Set<Genome> unspeciated = new HashSet<>(genomes.values());
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
		
		for (Species species : this.species.values()) {
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
		discardedSpecies.forEach(this::removeSpecies);
		
		// Get the compatibility threshold
		double compatibilityThreshold = jeat.config().species.compatibilityThreshold;
		// Speciate the remaining genomes
		for (Genome genome : unspeciated) {
			// Find the species with the closest representative genome
			Optional<Species> species = this.species.values()
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
		Set.copyOf(this.species.values()).stream().filter(Species::isEmpty).forEach(this::remove);
		
		// Reset the fitness of all species and genomes
		this.species.values().stream().peek(Species::resetFitness).forEach(Species::resetGenomeFitnesses);
	}

}
