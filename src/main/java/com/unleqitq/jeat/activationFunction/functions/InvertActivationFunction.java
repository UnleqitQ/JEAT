package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class InvertActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		if (value == 0) {
			return 0;
		}
		return 1 / value;
	}
	
}
