package com.unleqitq.jeat.jeat;

import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.gene.node.NodeGeneDefinition;
import com.unleqitq.jeat.genetics.genome.Genome;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Supplier;

public class NodeDefinitionStore {
	
	private final Map<UUID, NodeGeneDefinition<?, ?>> definitions = new TreeMap<>();
	private final Map<String, NodeGeneDefinition<?, ?>> namedDefinitions = new HashMap<>();
	
	public void add(NodeGeneDefinition<?, ?> definition) {
		definitions.put(definition.id(), definition);
		if (definition.name() != null) {
			namedDefinitions.put(definition.name(), definition);
		}
	}
	
	public NodeGeneDefinition<?, ?> get(UUID id) {
		return definitions.get(id);
	}
	
	public NodeGeneDefinition<?, ?> get(String name) {
		return namedDefinitions.get(name);
	}
	
	public NodeGeneDefinition<?, ?> get(UUID id, Supplier<NodeGeneDefinition<?, ?>> supplier) {
		return definitions.computeIfAbsent(id, k -> supplier.get());
	}
	
	public NodeGeneDefinition<?, ?> get(String name, Supplier<NodeGeneDefinition<?, ?>> supplier) {
		NodeGeneDefinition<?, ?> definition;
		if ((definition = get(name)) == null) {
			definition = supplier.get();
			add(definition);
		}
		return definition;
	}
	
	public int size() {
		return definitions.size();
	}
	
	public boolean has(UUID id) {
		return definitions.containsKey(id);
	}
	
	public boolean has(String name) {
		return namedDefinitions.containsKey(name);
	}
	
	public NodeGene<?, ?> createGene(UUID id, Genome genome) {
		NodeGeneDefinition<?, ?> definition = get(id);
		if (definition == null) {
			throw new IllegalArgumentException("No definition found for id " + id);
		}
		return definition.createGene(genome);
	}
	
	public NodeGene<?, ?> createGene(String name, Genome genome) {
		NodeGeneDefinition<?, ?> definition = get(name);
		if (definition == null) {
			throw new IllegalArgumentException("No definition found for name " + name);
		}
		return definition.createGene(genome);
	}
	
	public NodeGene<?, ?> createGene(UUID id, Genome genome, Supplier<NodeGeneDefinition<?, ?>> supplier) {
		NodeGeneDefinition<?, ?> definition = get(id, supplier);
		return definition.createGene(genome);
	}
	
	public NodeGene<?, ?> createGene(String name, Genome genome, Supplier<NodeGeneDefinition<?, ?>> supplier) {
		NodeGeneDefinition<?, ?> definition = get(name, supplier);
		return definition.createGene(genome);
	}
	
}
