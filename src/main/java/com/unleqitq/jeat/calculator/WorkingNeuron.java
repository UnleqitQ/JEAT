package com.unleqitq.jeat.calculator;

import com.unleqitq.jeat.activationFunction.ActivationFunctionReference;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGene;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorkingNeuron extends Neuron {
	
	private boolean calculated;
	private double value;
	@NotNull
	private final ActivationFunctionReference activationFunction;
	@NotNull
	private final AggregationFunction aggregationFunction;
	@NotNull
	private final List<Connection> connections;
	private final double response;
	
	WorkingNeuron(@NotNull WorkingNodeGene gene) {
		super(gene);
		this.activationFunction = gene.activationFunction().copy();
		this.aggregationFunction = gene.aggregationFunction();
		this.response = gene.response();
		connections = new ArrayList<>();
	}
	
	void addConnection(Connection connection) {
		connections.add(connection);
	}
	
	public void calculate() {
		if (calculated) {
			return;
		}
		List<Double> values = new ArrayList<>();
		for (Connection connection : connections) {
			values.add(connection.getValue());
		}
		double net = aggregationFunction.calculate(values);
		value = activationFunction.calculate(net) * response;
		calculated = true;
	}
	
	public double getValue() {
		calculate();
		return value;
	}
	
	public void reset() {
		calculated = false;
	}
	
}
