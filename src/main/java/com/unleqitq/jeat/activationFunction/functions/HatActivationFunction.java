package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class HatActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		double radius = parameters.get("radius").value();
		double center = parameters.get("center").value();
		return Math.max(0, 1 - Math.abs(value - center) / radius);
	}
	
	@Override
	@NotNull
	public Set<IParameterDefinition> parameters() {
		return Set.of(SimpleParameterDefinition.builder()
			.name("radius")
			.minValue(0.01)
			.maxValue(2)
			.defaultValue(1)
			.build(), SimpleParameterDefinition.builder()
			.name("center")
			.minValue(-2)
			.maxValue(2)
			.defaultValue(0)
			.build());
	}
	
}
