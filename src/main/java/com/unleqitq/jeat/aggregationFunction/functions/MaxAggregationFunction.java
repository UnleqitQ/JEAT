package com.unleqitq.jeat.aggregationFunction.functions;

import com.unleqitq.jeat.aggregationFunction.AggregationFunction;

import java.util.Collection;

public class MaxAggregationFunction implements AggregationFunction {
	
	@Override
	public double calculate(Collection<Double> values) {
		return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
	}
	
}
