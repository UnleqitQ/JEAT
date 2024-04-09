package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class AtanActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.atan(value) / Math.PI * 2;
	}
	
}
