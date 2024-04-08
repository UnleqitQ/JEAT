package com.unleqitq.jeat.calculator;

import com.unleqitq.jeat.genetics.gene.node.bias.BiasNodeGene;
import org.jetbrains.annotations.NotNull;

public class BiasNeuron extends Neuron {
	
	BiasNeuron(@NotNull BiasNodeGene gene) {
		super(gene);
	}
	
	@Override
	public double getValue() {
		return 1;
	}
	
}
