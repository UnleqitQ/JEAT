package com.unleqitq.jeat.population;


import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.internal.InternalUse;
import org.jetbrains.annotations.NotNull;

/**
 * This class contains helper methods for reproduction of the population.
 * <p>
 * This class is not intended to be used by the user.
 */
@InternalUse
public class ReproductionHelper {
	
	/**
	 * The Jeat instance
	 */
	@NotNull
	private final Jeat jeat;
	
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
	public ReproductionHelper(@NotNull Population population) {
		this.population = population;
		this.jeat = population.jeat();
	}
	
}
