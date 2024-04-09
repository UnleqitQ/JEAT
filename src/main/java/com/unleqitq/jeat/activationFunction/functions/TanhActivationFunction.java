package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class TanhActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.tanh(value);
	}
	
}
