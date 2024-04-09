package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class CubeActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		return Math.pow(value, 3);
	}
	
}
