package com.unleqitq.jeat.internal;

import lombok.experimental.UtilityClass;


/**
 * Global settings for JEAT, nothing of this really changes how the library works, it's just for
 * information purposes, and how some errors or states that might become a problem are handled.
 */
@UtilityClass
public class GlobalSettings {
	
	public static ErrorHandling NODE_X_BOUNDS = ErrorHandling.WARN;
	public static ErrorHandling BIAS_ALREADY_EXISTS = ErrorHandling.THROW;
	public static ErrorHandling COMPARING_DIFFERENT_NODE_TYPES = ErrorHandling.THROW;
	
	public enum ErrorHandling {
		THROW,
		WARN,
		IGNORE
	}
	
}
