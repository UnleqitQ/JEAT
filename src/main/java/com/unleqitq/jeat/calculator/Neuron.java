package com.unleqitq.jeat.calculator;

import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors (fluent = true)
@Getter
public abstract class Neuron {
	
	@Nullable
	private final String name;
	private final double x;
	private final boolean input;
	
	protected Neuron(double x, boolean input, @Nullable String name) {
		this.name = name;
		this.x = x;
		this.input = input;
	}
	
	protected Neuron(@NotNull NodeGene<?, ?> node) {
		this(node.x(), node.input(), node.name());
	}
	
	public abstract double getValue();
	
	public void reset() {
	}
	
}
