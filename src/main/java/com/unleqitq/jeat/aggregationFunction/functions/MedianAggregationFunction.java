package com.unleqitq.jeat.aggregationFunction.functions;

import com.unleqitq.jeat.aggregationFunction.AggregationFunction;

import java.util.Collection;
import java.util.List;

public class MedianAggregationFunction implements AggregationFunction {
	
	@Override
	public double calculate(Collection<Double> values) {
		if (values.isEmpty()) return 0.0;
		List<Double> sortedValues = values.stream().sorted().toList();
		int size = sortedValues.size();
		if (size % 2 == 0) return (sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2;
		else return sortedValues.get(size / 2);
	}
	
}
