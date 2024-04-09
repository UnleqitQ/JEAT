package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class SigmoidActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return 1 / (1 + Math.exp(-value));
	}
	
}
