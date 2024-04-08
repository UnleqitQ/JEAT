package com.unleqitq.jeat;

import com.unleqitq.jeat.config.JeatConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * Main class of the Java NEAT Port.
 *
 * @since 1.0
 */
@Accessors (fluent = true)
@Getter
public class Jeat {
	
	@NotNull
	private final JeatConfig config;
	
	public Jeat(@NotNull JeatConfig config) {
		this.config = config;
	}
	
}
