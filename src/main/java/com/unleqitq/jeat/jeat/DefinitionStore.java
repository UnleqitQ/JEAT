package com.unleqitq.jeat.jeat;

import com.unleqitq.jeat.genetics.gene.Gene;
import com.unleqitq.jeat.genetics.gene.GeneDefinition;
import com.unleqitq.jeat.genetics.genome.Genome;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class DefinitionStore<I extends Comparable<I>, D extends GeneDefinition<I, ?, ?>> {
	
	private final Map<I, D> definitions = new TreeMap<>();
	
	public void add(D definition) {
		definitions.put(definition.id(), definition);
	}
	
	public D get(I id) {
		return definitions.get(id);
	}
	
	public D get(I id, Supplier<D> supplier) {
		return definitions.computeIfAbsent(id, k -> supplier.get());
	}
	
	public boolean has(I id) {
		return definitions.containsKey(id);
	}
	
	public Gene<I, ?, ?> createGene(I id, Genome genome) {
		return get(id).createGene(genome);
	}
	
	public Gene<I, ?, ?> createGene(I id, Genome genome, Supplier<D> supplier) {
		return get(id, supplier).createGene(genome);
	}
	
}
