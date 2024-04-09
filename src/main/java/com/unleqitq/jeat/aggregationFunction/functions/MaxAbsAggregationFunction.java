package com.unleqitq.jeat.aggregationFunction.functions;

import com.unleqitq.jeat.aggregationFunction.AggregationFunction;

import java.util.Collection;
import java.util.Comparator;

public class MaxAbsAggregationFunction implements AggregationFunction {
	
	@Override
	public double calculate(Collection<Double> values) {
		return values.stream().max(Comparator.comparingDouble(Math::abs)).orElse(0.0);
	}
	
}
