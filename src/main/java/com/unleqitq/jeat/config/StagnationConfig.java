package com.unleqitq.jeat.config;

import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.ToDoubleFunction;

/**
 * Configuration for the stagnation of species
 */
@Builder
public class StagnationConfig {
	
	/**
	 * Function used to calculate the combined fitness of a species (this one is different from the one used for reproduction)
	 * <p>
	 * Default: Mean of the fitnesses of the genomes in the species
	 */
	@NotNull
	@Builder.Default
	public ToDoubleFunction<Collection<Genome>> fitnessFunction =
		genomes -> genomes.stream().mapToDouble(Genome::fitness).average().orElse(0);
	
	/**
	 * Number of generations without improvement to consider a species stagnant
	 * <p>
	 * Default: 15
	 */
	@Builder.Default
	public int maxStagnation = 15;
	
	/**
	 * Amount of species to protect from stagnation<br>
	 * The best species will always be protected<br>
	 * This is mainly important to avoid the extinction of species with high fitness but low diversity
	 */
	@Builder.Default
	public int speciesElitism = 3;
	
}
