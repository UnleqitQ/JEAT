package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.MutationConfig;
import com.unleqitq.jeat.genetics.gene.node.AbstractNodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Accessors (fluent = true)
@Getter
@Setter
public class WorkingNodeGene extends AbstractNodeGene<WorkingNodeGene, WorkingNodeGeneDefinition> {
	
	private ActivationFunction activationFunction;
	private AggregationFunction aggregationFunction;
	private boolean enabled = true;
	
	public WorkingNodeGene(@NotNull Genome genome, @NotNull WorkingNodeGeneDefinition definition) {
		super(genome, definition);
	}
	
	@Override
	public void mutate() {
		MutationConfig.NodeMutationConfig config = jeat().config().mutation.node;
		Random rnd = jeat().random();
		if (definition().lockedActivationFunction() == null &&
			rnd.nextDouble() < config.activation.changeActivationFunctionChance) {
			activationFunction = jeat().activationFunctions().getRandomElement(rnd, activationFunction);
		}
		if (definition().lockedAggregationFunction() == null &&
			rnd.nextDouble() < config.aggregation.changeAggregationFunctionChance) {
			aggregationFunction = jeat().aggregationFunctions().getRandomElement(rnd, aggregationFunction);
		}
		if (definition().canDisable() && rnd.nextDouble() < config.structure.toggleNodeChance) {
			enabled = !enabled;
		}
	}
	
	@Override
	@NotNull
	public WorkingNodeGene copy(@NotNull Genome genome) {
		WorkingNodeGene copy = new WorkingNodeGene(genome, definition());
		copy.enabled = enabled;
		copy.activationFunction = activationFunction;
		copy.aggregationFunction = aggregationFunction;
		return copy;
	}
	
}
