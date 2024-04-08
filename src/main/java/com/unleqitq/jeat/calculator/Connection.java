package com.unleqitq.jeat.calculator;

import org.jetbrains.annotations.NotNull;

public record Connection(@NotNull Neuron neuron, double weight) {
	
	public double getValue() {
		return neuron.getValue() * weight;
	}
	
}
