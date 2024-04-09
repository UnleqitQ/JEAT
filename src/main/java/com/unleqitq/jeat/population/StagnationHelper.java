package com.unleqitq.jeat.population;

import com.unleqitq.jeat.internal.InternalUse;
import org.jetbrains.annotations.NotNull;

/**
 * This class contains helper methods to detect stagnation in the population.
 * <p>
 * This class is not intended to be used by the user.
 */
@InternalUse
public class StagnationHelper {
	
	/**
	 * The population to which this helper belongs.
	 */
	@NotNull
	private final Population population;
	
	/**
	 * Creates a new instance of this class.
	 *
	 * @param population The population to which this helper belongs.
	 */
	public StagnationHelper(@NotNull Population population) {
		this.population = population;
	}
	
}
