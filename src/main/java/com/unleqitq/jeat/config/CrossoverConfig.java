package com.unleqitq.jeat.config;

import lombok.Builder;

@Builder
public class CrossoverConfig {
	
	/**
	 * The probability that a gene will be inherited from the first parent during crossover.<br>
	 * (the probability that a gene will be inherited from the second parent is 1 - geneInheritanceProbability)
	 */
	@Builder.Default
	public double geneInheritanceProbability = 0.5;
	
	/**
	 * Whether the connections should be thinned out after crossover.
	 */
	@Builder.Default
	public boolean thinOutConnections = true;
	
	/**
	 * The maximum percentage of connections that should be thinned out after crossover.<br>
	 * (The actual percentage will be a random value between 0 and this value)
	 */
	@Builder.Default
	public double thinOutPercentage = 0.2;
	
}
