package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class LeakyReLUActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		double alpha = parameters.get("alpha").value();
		return value > 0 ? value : value * Math.pow(10, alpha);
	}
	
	@Override
	@NotNull
	public Set<IParameterDefinition> parameters() {
		return Set.of(SimpleParameterDefinition.builder()
			.name("alpha")
			.minValue(-5)
			.maxValue(0)
			.defaultValue(-2)
			.build());
	}
	
}
