package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGeneDefinition;
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
	extends AbstractNodeGeneDefinition<WorkingNodeGeneDefinition, WorkingNodeGene> {
	
	@Nullable
	private ActivationFunction lockedActivationFunction;
	@Nullable
	private AggregationFunction lockedAggregationFunction;
	private boolean canDisable = true;
	
	public WorkingNodeGeneDefinition(@NotNull UUID id, double x, @Nullable String name) {
		super(id, x, false, name);
	}
	
	public WorkingNodeGeneDefinition(double x, @Nullable String name) {
		super(x, false, name);
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
	
}
