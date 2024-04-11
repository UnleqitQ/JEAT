package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GaussActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		double sigma = parameters.get("sigma").value();
		double mu = parameters.get("mu").value();
		return Math.exp(-Math.pow(value - mu, 2) / (2 * Math.pow(sigma, 2)));
	}
	
	@Override
	@NotNull
	public Set<IParameterDefinition> parameters() {
		return Set.of(SimpleParameterDefinition.builder()
			.name("sigma")
			.minValue(0.01)
			.maxValue(2)
			.defaultValue(0.5)
			.build(), SimpleParameterDefinition.builder()
			.name("mu")
			.minValue(-2)
			.maxValue(2)
			.defaultValue(0)
			.build());
	}
	
}
