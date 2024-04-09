package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class HatActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.max(0, 1 - Math.abs(value));
	}
	
}
