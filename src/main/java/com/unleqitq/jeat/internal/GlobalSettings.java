package com.unleqitq.jeat.internal;

import lombok.experimental.UtilityClass;


/**
 * Global settings for JEAT, nothing of this really changes how the library works, it's just for
 * information purposes, and how some errors or states that might become a problem are handled.
 */
@UtilityClass
public class GlobalSettings {
	
	public static final String VERSION = "1.0.0";
	
	public static final String NAME = "JEAT";
	
	public static final String AUTHOR = "UnleqitQ";
	
	public static final ErrorHandling NODE_X_BOUNDS = ErrorHandling.WARN;
	public static final ErrorHandling BIAS_ALREADY_EXISTS = ErrorHandling.THROW;
	
	public enum ErrorHandling {
		THROW,
		WARN,
		IGNORE
	}
	
}
