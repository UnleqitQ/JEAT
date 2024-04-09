package com.unleqitq.jeat.genetics.species;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Represents a species in the NEAT algorithm
 */
@Accessors (fluent = true)
public class Species implements Comparable<Species> {
	
	/**
	 * The Jeat instance
	 */
	@Getter
	@NotNull
	private final Jeat jeat;
	
	/**
	 * The id of the species
	 */
	@Getter
	@NotNull
	private final UUID id;
	
	/**
	 * The fitness of the species
	 */
	@Nullable
	private Double fitness;
	
	/**
	 * The genomes in the species
	 */
	@NotNull
	private final Map<UUID, Genome> genomes = new HashMap<>();
	
	/**
	 * The stagnation fitness of the species
	 */
	@Nullable
	private Double stagnationFitness;
	
	/**
	 * The history of the stagnation fitness of the species
	 */
	@NotNull
	private final List<Double> stagnationHistory = new ArrayList<>();
	
	/**
	 * The representative genome of the species
	 */
	@NotNull
	@Getter
	@Setter
	private Genome representative;
	
	/**
	 * Create a new species with a given id
	 * @param id The id of the species
	 */
	public Species(@NotNull Jeat jeat, @NotNull UUID id, @NotNull Genome representative) {
		this.jeat = jeat;
		this.id = id;
		this.representative = representative;
		this.add(representative);
	}
	
	/**
	 * Create a new species with a random id
	 */
	public Species(@NotNull Jeat jeat, @NotNull Genome representative) {
		this(jeat, UUID.randomUUID(), representative);
	}
	
	/**
	 * Calculate the combined fitness of all genomes in the species
	 * @param aggregationFunction The function to aggregate the fitness of all genomes
	 */
	public void calculateFitness(@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		this.fitness = aggregationFunction.applyAsDouble(this.genomes.values());
	}
	
	/**
	 * Get the fitness of the species
	 * @return The fitness of the species
	 */
	public double fitness() {
		if (this.fitness == null) throw new IllegalStateException("Fitness not calculated");
		return this.fitness;
	}
	
	/**
	 * Get the fitness of the species
	 * @param aggregationFunction The function to aggregate the fitness of all genomes
	 *                            if the fitness is not calculated
	 * @return The fitness of the species
	 */
	public double fitness(@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		if (this.fitness == null) this.calculateFitness(aggregationFunction);
		return this.fitness;
	}
	
	/**
	 * Add a genome to the species
	 * @param genome The genome to add
	 */
	public void add(@NotNull Genome genome) {
		this.genomes.put(genome.id(), genome);
		this.fitness = null;
	}
	
	/**
	 * Remove a genome from the species
	 * @param genome The genome to remove
	 */
	public void removeGenome(@NotNull Genome genome) {
		this.genomes.remove(genome.id());
	}
	
	/**
	 * Remove a genome from the species
	 * @param genomeId The id of the genome to remove
	 */
	public void removeGenome(@NotNull UUID genomeId) {
		this.genomes.remove(genomeId);
	}
	
	/**
	 * Get the genomes in the species
	 * @return The genomes in the species
	 */
	@NotNull
	public Collection<Genome> getGenomes() {
		return this.genomes.values();
	}
	
	/**
	 * Get the number of genomes in the species
	 * @return The number of genomes in the species
	 */
	public int size() {
		return this.genomes.size();
	}
	
	/**
	 * Check if the species is empty
	 * @return True if the species is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.genomes.isEmpty();
	}
	
	/**
	 * Clear the species
	 */
	public void clear() {
		this.genomes.clear();
		this.fitness = null;
	}
	
	/**
	 * Set the fitness of the species to null
	 */
	public void resetFitness() {
		this.fitness = null;
	}
	
	/**
	 * Set the fitnesses of all genomes in the species to 0
	 */
	public void resetGenomeFitnesses() {
		this.genomes.values().forEach(g -> g.fitness(0));
	}
	
	/**
	 * Get all fitness values of the genomes in the species
	 * @return The fitness values of the genomes in the species
	 */
	@NotNull
	public Map<UUID, Double> getGenomeFitnesses() {
		return this.genomes.values().stream().collect(Collectors.toMap(Genome::id, Genome::fitness));
	}
	
	/**
	 * The comparison function uses the id of the species to compare them<br>
	 * This is mainly used for TreeSet and TreeMap
	 *
	 * @param o The species to compare to
	 * @return The comparison result
	 */
	@Override
	public int compareTo(@NotNull Species o) {
		return this.id.compareTo(o.id);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Species species = (Species) o;
		return id.equals(species.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * Calculate the stagnation fitness of the species
	 */
	public void calculateStagnationFitness() {
		this.stagnationFitness =
			jeat.config().stagnation.fitnessFunction.applyAsDouble(genomes.values());
	}
	
	/**
	 * Get the stagnation fitness of the species
	 * @return The stagnation fitness of the species
	 */
	public double stagnationFitness() {
		if (this.stagnationFitness == null) calculateStagnationFitness();
		return this.stagnationFitness;
	}
	
	/**
	 * Reset the stagnation fitness of the species
	 */
	public void resetStagnationFitness() {
		this.stagnationFitness = null;
	}
	
	/**
	 * Store the stagnation fitness of the species in the history
	 */
	public void storeStagnationFitness() {
		this.stagnationHistory.add(stagnationFitness());
	}
	
	/**
	 * Get the history of the stagnation fitness of the species
	 * @return The history of the stagnation fitness of the species
	 */
	@NotNull
	public List<Double> stagnationHistory() {
		return Collections.unmodifiableList(this.stagnationHistory);
	}
	
}
