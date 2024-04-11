package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class SigmoidActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value, @NotNull Map<String, IParameter> parameters) {
		// I know L is redundant with response, but I'm not going to change it,
		// and it's not as if it would matter anyway
		double L = parameters.get("L").value();
		double k = parameters.get("k").value();
		double x0 = parameters.get("x0").value();
		return L / (1 + Math.exp(-k * (value - x0)));
	}
	
	@Override
	@NotNull
	public Set<IParameterDefinition> parameters() {
		return Set.of(SimpleParameterDefinition.builder()
			.name("L")
			.minValue(0.01)
			.maxValue(3)
			.defaultValue(1)
			.build(), SimpleParameterDefinition.builder()
			.name("k")
			.minValue(0.01)
			.maxValue(3)
			.defaultValue(1)
			.build(), SimpleParameterDefinition.builder()
			.name("x0")
			.minValue(-2)
			.maxValue(2)
			.defaultValue(0)
			.build());
	}
	
}
