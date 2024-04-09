package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class SoftplusActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.log(1 + Math.exp(value));
	}
	
}
