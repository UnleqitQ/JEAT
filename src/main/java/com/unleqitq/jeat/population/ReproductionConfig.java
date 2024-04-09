package com.unleqitq.jeat.population;

import lombok.Builder;

/**
 * This class contains the configuration for the reproduction of the population.
 */
@Builder
public class ReproductionConfig {
	
	/**
	 * Whether to use sexual or asexual reproduction.<br>
	 * If {@code true}, sexual reproduction will be used,
	 * otherwise asexual reproduction will be used.
	 */
	@Builder.Default
	public boolean sexualReproduction = true;
	
	/**
	 * The minimal size of a species to be allowed to reproduce.
	 */
	@Builder.Default
	public int minimalSpeciesSize = 10;
	
	/**
	 * The minimal amount of species to be used for reproduction.<br>
	 * If the amount of species is smaller than this value,
	 * first {@link #discardRatio} will be reduced,
	 * then {@link #minimalSpeciesSize} will be increased to reach this value.
	 */
	@Builder.Default
	public int minimalSpeciesAmount = 5;
	
	/**
	 * The ratio of the genomes of a species that will be kept for the next generation.
	 * (depending on the fitness of the genomes)<br>
	 * The rest will be used for the reproduction but will not be kept for the next generation.
	 */
	@Builder.Default
	public double survivalRate = 0.1;
	
	/**
	 * The ratio of species that will be discarded before reproduction. (the worst species)
	 */
	@Builder.Default
	public double discardRatio = 0.1;
	
	/**
	 * The ratio of genomes that will have a copy of themselves
	 * in the next generation that is not mutated. (the best genomes)<br>
	 * This is used to make sure that the population does not get worse over time.<br>
	 * The genomes are selected by the fitness of the genomes
	 * and completely independent of the species.
	 */
	@Builder.Default
	public double elitismRatio = 0.05;
	
}
