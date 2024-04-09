package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class ExpActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.exp(value);
	}
	
}
