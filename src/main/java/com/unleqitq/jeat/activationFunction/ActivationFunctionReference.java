package com.unleqitq.jeat.activationFunction;

import com.unleqitq.jeat.Jeat;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a reference to an activation function
 */
@Accessors (fluent = true)
@Getter
public class ActivationFunctionReference {
	
	/**
	 * The jeat instance
	 */
	@NotNull
	private final Jeat jeat;
	
	/**
	 * The activation function that is referenced
	 */
	@NotNull
	private final ActivationFunction activationFunction;
	
	/**
	 * The parameters of the activation function
	 */
	@NotNull
	private final Map<String, ActivationFunction.IParameter> parameters;
	
	/**
	 * Creates a new activation function reference
	 * @param jeat The jeat instance
	 * @param activationFunction The activation function that is referenced
	 */
	public ActivationFunctionReference(@NotNull Jeat jeat,
		@NotNull ActivationFunction activationFunction) {
		this.jeat = jeat;
		this.activationFunction = activationFunction;
		Set<ActivationFunction.IParameterDefinition> parameterDefinitions =
			activationFunction.parameters();
		this.parameters = parameterDefinitions.stream()
			.collect(Collectors.toMap(ActivationFunction.IParameterDefinition::name,
				parameterDefinition -> parameterDefinition.create(jeat)));
	}
	
	/**
	 * Creates a new activation function reference
	 * @param jeat The jeat instance
	 * @param activationFunction The activation function that is referenced
	 * @param parameters The parameters of the activation function
	 */
	public ActivationFunctionReference(@NotNull Jeat jeat,
		@NotNull ActivationFunction activationFunction,
		@NotNull Map<String, ActivationFunction.IParameter> parameters) {
		this.jeat = jeat;
		this.activationFunction = activationFunction;
		this.parameters = parameters;
	}
	
	/**
	 * Calculates the value of the activation function
	 */
	public double calculate(double value) {
		return activationFunction.calculate(value, parameters);
	}
	
	/**
	 * Mutates the parameters of the activation function
	 */
	public void mutate() {
		parameters.values().forEach(ActivationFunction.IParameter::mutate);
	}
	
	/**
	 * Creates a copy of the activation function reference
	 */
	public ActivationFunctionReference copy() {
		return new ActivationFunctionReference(jeat, activationFunction, parameters.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().copy())));
	}
	
}
