package com.unleqitq.jeat.config;

import lombok.Builder;

@Builder
public class SpeciesConfig {
	
	/**
	 * The compatibility threshold for species
	 */
	@Builder.Default
	public double compatibilityThreshold = 3.0;
	
}
