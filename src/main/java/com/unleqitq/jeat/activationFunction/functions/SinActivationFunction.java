package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class SinActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		if (value > 1) {
			return 1;
		} else if (value < -1) {
			return -1;
		}
		return Math.sin(value*Math.PI/2);
	}
	
}
