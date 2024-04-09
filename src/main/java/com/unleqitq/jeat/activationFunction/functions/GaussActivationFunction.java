package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class GaussActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.exp(-Math.pow(value, 2));
	}
	
}
