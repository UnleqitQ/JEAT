package com.unleqitq.jeat.config;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public class JeatConfig {
	
	/**
	 * The configuration for the mutation process.
	 */
	@NotNull
	public MutationConfig mutation;
	
	/**
	 * The configuration for the initial structure of the genome.
	 */
	@NotNull
	public InitialStructureConfig initialStructure;
	
	/**
	 * The configuration for the crossover process.
	 */
	@NotNull
	public CrossoverConfig crossover;
	
}
