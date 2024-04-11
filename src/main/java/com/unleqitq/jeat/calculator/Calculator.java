package com.unleqitq.jeat.calculator;

import com.unleqitq.jeat.genetics.gene.connection.ConnectionGene;
import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.gene.node.bias.BiasNodeGene;
import com.unleqitq.jeat.genetics.gene.node.input.InputNodeGene;
import com.unleqitq.jeat.genetics.gene.node.working.WorkingNodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Calculator {
	
	@Getter
	@NotNull
	private final Genome genome;
	@NotNull
	private final Map<String, Neuron> namedNeurons = new HashMap<>();
	@NotNull
	private final List<InputNeuron> inputNeurons = new ArrayList<>();
	@NotNull
	private final List<WorkingNeuron> outputNeurons = new ArrayList<>();
	@NotNull
	private final List<Neuron> neurons = new ArrayList<>();
	
	private Calculator(@NotNull Genome genome) {
		this.genome = genome;
	}
	
	void addNeuron(@NotNull Neuron neuron) {
		neurons.add(neuron);
		if (neuron.name() != null) {
			namedNeurons.put(neuron.name(), neuron);
		}
	}
	
	public double getFitness() {
		return genome.fitness();
	}
	
	public void setFitness(double fitness) {
		genome.fitness(fitness);
	}
	
	@NotNull
	public List<Neuron> getNeurons() {
		return Collections.unmodifiableList(neurons);
	}
	
	@NotNull
	public List<InputNeuron> getInputNeurons() {
		return Collections.unmodifiableList(inputNeurons);
	}
	
	@NotNull
	public List<WorkingNeuron> getOutputNeurons() {
		return Collections.unmodifiableList(outputNeurons);
	}
	
	@NotNull
	public List<String> getInputNames() {
		return inputNeurons.stream().map(Neuron::name).toList();
	}
	
	@NotNull
	public List<String> getOutputNames() {
		return outputNeurons.stream().map(Neuron::name).toList();
	}
	
	public void setInput(@NotNull String name, double value) {
		Neuron neuron = namedNeurons.get(name);
		if (neuron == null) {
			throw new IllegalArgumentException("No neuron with name " + name);
		}
		if (!(neuron instanceof InputNeuron)) {
			throw new IllegalArgumentException("Neuron with name " + name + " is not an input neuron");
		}
		((InputNeuron) neuron).setValue(value);
	}
	
	public double getOutput(@NotNull String name) {
		Neuron neuron = namedNeurons.get(name);
		if (neuron == null) {
			throw new IllegalArgumentException("No neuron with name " + name);
		}
		if (!(neuron instanceof WorkingNeuron)) {
			throw new IllegalArgumentException("Neuron with name " + name + " is not a working neuron");
		}
		return neuron.getValue();
	}
	
	public void reset() {
		neurons.forEach(Neuron::reset);
	}
	
	@NotNull
	public Map<String, Double> feedForward(@NotNull Map<String, Double> inputs) {
		reset();
		inputs.forEach(this::setInput);
		return outputNeurons.stream()
			.collect(HashMap::new, (map, neuron) -> map.put(neuron.name(), neuron.getValue()),
				HashMap::putAll);
	}
	
	@NotNull
	public static Calculator create(@NotNull Genome genome) {
		Calculator calculator = new Calculator(genome);
		Map<UUID, Neuron> neuronIdMap = new TreeMap<>();
		for (NodeGene<?, ?> gene : genome.nodes().values()) {
			switch (gene) {
				case BiasNodeGene b -> {
					BiasNeuron neuron = new BiasNeuron(b);
					calculator.addNeuron(neuron);
					neuronIdMap.put(b.id(), neuron);
				}
				case InputNodeGene i -> {
					InputNeuron neuron = new InputNeuron(i);
					calculator.addNeuron(neuron);
					calculator.inputNeurons.add(neuron);
					neuronIdMap.put(i.id(), neuron);
				}
				case WorkingNodeGene w -> {
					if (!w.enabled()) {
						neuronIdMap.put(w.id(), new DisabledNeuron(w));
						break;
					}
					WorkingNeuron neuron = new WorkingNeuron(w);
					calculator.addNeuron(neuron);
					if (w.name() != null) calculator.outputNeurons.add(neuron);
					neuronIdMap.put(w.id(), neuron);
				}
				case null, default ->
					throw new IllegalArgumentException("Unknown node gene type: " + gene.getClass());
			}
		}
		for (ConnectionGene gene : genome.connections().values()) {
			if (!gene.enabled()) {
				// No need to create disabled connections
				continue;
			}
			Neuron from = neuronIdMap.get(gene.fromId());
			Neuron to = neuronIdMap.get(gene.toId());
			if (from == null || to == null) {
				throw new IllegalArgumentException("Connection gene references non-existent neuron");
			}
			if (from instanceof DisabledNeuron || to instanceof DisabledNeuron) {
				// Don't create connections to or from disabled neurons
				continue;
			}
			if (to instanceof WorkingNeuron) {
				((WorkingNeuron) to).addConnection(new Connection(from, gene.weight()));
			}
			else {
				throw new IllegalArgumentException(
					"Connection gene references non-working neuron as target");
			}
		}
		return calculator;
	}
	
	/**
	 * This is only used for the creation of the neural network from the genome.
	 * They are not added to the network in the first place.
	 */
	private static class DisabledNeuron extends Neuron {
		
		DisabledNeuron(@NotNull NodeGene<?, ?> gene) {
			super(gene);
		}
		
		@Override
		public double getValue() {
			throw new IllegalStateException("Disabled neurons cannot be used");
		}
		
	}
	
}
