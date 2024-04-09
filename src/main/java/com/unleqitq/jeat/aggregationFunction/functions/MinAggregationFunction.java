package com.unleqitq.jeat.aggregationFunction.functions;

import com.unleqitq.jeat.aggregationFunction.AggregationFunction;

import java.util.Collection;

public class MinAggregationFunction implements AggregationFunction {
	
	@Override
	public double calculate(Collection<Double> values) {
		return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
	}
	
}
