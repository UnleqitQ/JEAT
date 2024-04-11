package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class ClampedActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		double clampRange = parameters.get("range").value();
		return Math.clamp(value, -clampRange, clampRange);
	}
	
	@Override
	@NotNull
	public Set<IParameterDefinition> parameters() {
		return Set.of(
			SimpleParameterDefinition.builder().minValue(0.1).maxValue(10).defaultValue(1.0).name("range").build());
	}
	
}
