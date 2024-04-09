package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class ClampedActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.clamp(value, -1, 1);
	}
	
}
