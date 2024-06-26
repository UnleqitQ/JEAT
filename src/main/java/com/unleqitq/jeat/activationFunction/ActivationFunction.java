package com.unleqitq.jeat.activationFunction;

import com.unleqitq.jeat.Jeat;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ActivationFunction {
	
	default double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		return calculate(value);
	}
	
	default double calculate(double value) {
		throw new UnsupportedOperationException(
			"This activation function does not support the calculation of the value without parameters");
	}
	
	/**
	 * @return the name of the activation function
	 */
	@NotNull
	default Set<IParameterDefinition> parameters() {
		return Set.of();
	}
	
	/**
	 * calculates the distance between two parameter sets
	 * @param parameters1 the first set of parameters
	 * @param parameters2 the second set of parameters
	 */
	default double distance(@NotNull Collection<IParameter> parameters1,
		@NotNull Collection<IParameter> parameters2) {
		double distance = 0;
		for (IParameter parameter1 : parameters1) {
			IParameter parameter2 = parameters2.stream()
				.filter(p -> p.name().equals(parameter1.name()))
				.findFirst()
				.orElse(null);
			if (parameter2 != null) {
				distance += Math.abs(parameter1.value() - parameter2.value());
			}
		}
		return distance;
	}
	
	
	interface IParameterDefinition {
		
		/**
		 * @return the name of the parameter
		 */
		@NotNull
		String name();
		
		/**
		 * @param jeat the jeat instance
		 * @return an instance of the parameter
		 */
		@NotNull
		IParameter create(@NotNull Jeat jeat);
		
	}
	
	interface IParameter {
		
		/**
		 * @return the name of the parameter
		 */
		@NotNull
		String name();
		
		/**
		 * @return the value of the parameter
		 */
		double value();
		
		/**
		 * @return the definition of the parameter
		 */
		@NotNull
		IParameterDefinition definition();
		
		/**
		 * Mutates the value of the parameter
		 */
		void mutate();
		
		/**
		 * Creates a copy of the parameter
		 * @return the copy of the parameter
		 */
		@NotNull
		IParameter copy();
		
	}
	
	@Builder
	@Accessors (fluent = true)
	@Getter
	class SimpleParameterDefinition implements IParameterDefinition {
		
		/**
		 * The name of the parameter
		 */
		@NotNull
		private final String name;
		
		/**
		 * The default value of the parameter
		 */
		@Builder.Default
		private final double defaultValue = 0.0;
		
		/**
		 * The mutation chance of the parameter
		 */
		@Builder.Default
		private final double mutationChance = 0.1;
		
		/**
		 * The mutation range of the parameter
		 */
		@Builder.Default
		private final double mutationRange = 0.1;
		
		/**
		 * The minimum value of the parameter
		 */
		@Builder.Default
		private double minValue = Double.MIN_VALUE;
		
		/**
		 * The maximum value of the parameter
		 */
		@Builder.Default
		private double maxValue = Double.MAX_VALUE;
		
		/**
		 * Invalid values for the parameter
		 */
		@Singular
		@NotNull
		private Set<Double> invalidValues;
		
		@Override
		@NotNull
		public IParameter create(@NotNull Jeat jeat) {
			return new SimpleParameter(jeat, name, this);
		}
		
	}
	
	@Accessors (fluent = true)
	@Getter
	class SimpleParameter implements IParameter {
		
		/**
		 * The jeat instance
		 */
		@NotNull
		private final Jeat jeat;
		
		/**
		 * The name of the parameter
		 */
		@NotNull
		private final String name;
		
		/**
		 * The definition of the parameter
		 */
		@NotNull
		private final SimpleParameterDefinition definition;
		
		/**
		 * The value of the parameter
		 */
		private double value;
		
		public SimpleParameter(@NotNull Jeat jeat, @NotNull String name,
			@NotNull SimpleParameterDefinition definition) {
			this.jeat = jeat;
			this.name = name;
			this.definition = definition;
			this.value = definition.defaultValue();
		}
		
		
		@Override
		public void mutate() {
			final int maxTries = 100;
			if (jeat.random().nextDouble() < definition.mutationChance()) {
				double newValue;
				int tries = 0;
				while (true) {
					newValue = Math.clamp(value +
							jeat.random().nextDouble(-definition.mutationRange(), definition.mutationRange()),
						definition.minValue(), definition.maxValue());
					if (tries++ > maxTries) {
						Jeat.LOGGER.debug("Could not find a valid value for the parameter {} after {} tries",
							name, maxTries);
						return;
					}
					if (!definition.invalidValues().contains(newValue)) {
						break;
					}
					Jeat.LOGGER.debug("Invalid value for parameter {}: {}", name, newValue);
				}
				value = newValue;
			}
		}
		
		@Override
		@NotNull
		public SimpleParameter copy() {
			SimpleParameter copy = new SimpleParameter(jeat, name, definition);
			copy.value = value;
			return copy;
		}
		
	}
	
}
