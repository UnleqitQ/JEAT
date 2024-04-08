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
	
}
