package com.unleqitq.jeat.aggregationFunction.functions;

import com.unleqitq.jeat.aggregationFunction.AggregationFunction;

import java.util.Collection;

public class MeanAggregationFunction implements AggregationFunction {
	
	@Override
	public double calculate(Collection<Double> values) {
		return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
	}
	
}
