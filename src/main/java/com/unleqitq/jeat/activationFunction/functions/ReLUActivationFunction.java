package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class ReLUActivationFunction implements ActivationFunction {
	
	public double calculate(double value) {
		return Math.max(0, value);
	}
	
}
