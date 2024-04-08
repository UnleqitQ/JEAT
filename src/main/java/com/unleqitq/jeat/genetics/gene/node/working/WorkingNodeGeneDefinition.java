package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.genetics.gene.node.NodeGeneDefinition;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Accessors (fluent = true)
@Getter
@Setter
public class WorkingNodeGeneDefinition
	extends NodeGeneDefinition<WorkingNodeGeneDefinition, WorkingNodeGene> {
	
	@Nullable
	private ActivationFunction lockedActivationFunction;
	@Nullable
	private AggregationFunction lockedAggregationFunction;
	private boolean canDisable = true;
	private boolean removable = true;
	
	public WorkingNodeGeneDefinition(@NotNull UUID id, double x, @Nullable String name) {
		super(id, x, false, name);
	}
	
	public WorkingNodeGeneDefinition(double x, @Nullable String name) {
		super(x, false, name);
	}
	
	public WorkingNodeGeneDefinition(@NotNull UUID id, double x) {
		super(id, x, false, null);
	}
	
	public WorkingNodeGeneDefinition(double x) {
		super(x, false, null);
	}
	
	@Override
	@NotNull
	public WorkingNodeGene createGene(@NotNull Genome genome) {
		WorkingNodeGene gene = new WorkingNodeGene(genome, this);
		if (lockedActivationFunction != null) {
			gene.activationFunction(lockedActivationFunction);
		}
		if (lockedAggregationFunction != null) {
			gene.aggregationFunction(lockedAggregationFunction);
		}
		return gene;
	}
	
	@Override
	public boolean canRemove() {
		return removable;
	}
	
}
