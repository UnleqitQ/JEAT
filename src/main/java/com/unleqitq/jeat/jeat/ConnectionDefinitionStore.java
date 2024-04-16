package com.unleqitq.jeat.jeat;

import com.unleqitq.jeat.genetics.gene.connection.ConnectionGene;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGeneDefinition;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionIdentifier;
import com.unleqitq.jeat.genetics.genome.Genome;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class ConnectionDefinitionStore {
	
	private final Map<ConnectionIdentifier, ConnectionGeneDefinition> definitions = new TreeMap<>();
	
	public void add(ConnectionGeneDefinition definition) {
		definitions.put(definition.id(), definition);
	}
	
	public ConnectionGeneDefinition get(ConnectionIdentifier id) {
		return definitions.get(id);
	}
	
	public ConnectionGeneDefinition get(ConnectionIdentifier id, Supplier<ConnectionGeneDefinition> supplier) {
		return definitions.computeIfAbsent(id, k -> supplier.get());
	}
	
	public int size() {
		return definitions.size();
	}
	
	public boolean has(ConnectionIdentifier id) {
		return definitions.containsKey(id);
	}
	
	public ConnectionGene createGene(ConnectionIdentifier id, Genome genome) {
		return get(id).createGene(genome);
	}
	
	public ConnectionGene createGene(ConnectionIdentifier id, Genome genome, Supplier<ConnectionGeneDefinition> supplier) {
		return get(id, supplier).createGene(genome);
	}
	
}
