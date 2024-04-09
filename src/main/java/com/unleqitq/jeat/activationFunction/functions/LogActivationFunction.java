package com.unleqitq.jeat.activationFunction.functions;

import com.unleqitq.jeat.activationFunction.ActivationFunction;

public class LogActivationFunction implements ActivationFunction {
	
	@Override
	public double calculate(double value) {
		if (Math.abs(value) < 1e-6) {
			return 0;
		}
		return Math.log(Math.abs(value));
	}
	
}
