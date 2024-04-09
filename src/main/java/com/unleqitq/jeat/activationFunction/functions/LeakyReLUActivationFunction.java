package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class LeakyReLUActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return value > 0 ? value : 0.01 * value;
	}
	
}
