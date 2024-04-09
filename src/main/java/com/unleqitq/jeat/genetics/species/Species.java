package com.unleqitq.jeat.genetics.species;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Represents a species in the NEAT algorithm
 */
@Accessors (fluent = true)
public class Species {
	
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
	 * The representative genome of the species
	 */
	@NotNull
	@Getter
	private Genome representative;
	
	/**
	 * Create a new species with a given id
	 * @param id The id of the species
	 */
	public Species(@NotNull Jeat jeat, @NotNull UUID id, @NotNull Genome representative) {
		this.jeat = jeat;
		this.id = id;
		this.representative = representative;
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
	public double getFitness() {
		if (this.fitness == null) throw new IllegalStateException("Fitness not calculated");
		return this.fitness;
	}
	
	/**
	 * Get the fitness of the species
	 * @param aggregationFunction The function to aggregate the fitness of all genomes
	 *                            if the fitness is not calculated
	 * @return The fitness of the species
	 */
	public double getFitness(@NotNull ToDoubleFunction<Collection<Genome>> aggregationFunction) {
		if (this.fitness == null) this.calculateFitness(aggregationFunction);
		return this.getFitness();
	}
	
	/**
	 * Add a genome to the species
	 * @param genome The genome to add
	 */
	public void addGenome(@NotNull Genome genome) {
		this.genomes.put(genome.id(), genome);
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
	}
	
	/**
	 * Get all fitness values of the genomes in the species
	 * @return The fitness values of the genomes in the species
	 */
	@NotNull
	public Map<UUID, Double> getGenomeFitnesses() {
		return this.genomes.values().stream().collect(Collectors.toMap(Genome::id, Genome::fitness));
	}
	
}
