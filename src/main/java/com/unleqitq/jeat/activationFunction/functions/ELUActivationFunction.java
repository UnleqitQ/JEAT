package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class ELUActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		if (value > 0) {
			return value;
		}
		else {
			return Math.exp(value) - 1;
		}
	}
	
}
