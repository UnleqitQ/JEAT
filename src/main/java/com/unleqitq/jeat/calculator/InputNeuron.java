package com.unleqitq.jeat.calculator;

import com.unleqitq.jeat.genetics.gene.node.input.InputNodeGene;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
public class InputNeuron extends Neuron {
	
	private double value;
	
	InputNeuron(@NotNull InputNodeGene gene) {
		super(gene);
	}
	
	@Override
	public double getValue() {
		return value;
	}
	
}
