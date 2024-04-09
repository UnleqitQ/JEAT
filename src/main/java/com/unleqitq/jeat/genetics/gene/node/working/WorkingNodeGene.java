package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.MutationConfig;
import com.unleqitq.jeat.genetics.gene.node.NodeGene;
import com.unleqitq.jeat.genetics.genome.Genome;
import com.unleqitq.jeat.internal.GlobalSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Accessors (fluent = true)
@Getter
@Setter
public class WorkingNodeGene extends NodeGene<WorkingNodeGene, WorkingNodeGeneDefinition> {
	
	private ActivationFunction activationFunction;
	private AggregationFunction aggregationFunction;
	private boolean enabled = true;
	
	public WorkingNodeGene(@NotNull Genome genome, @NotNull WorkingNodeGeneDefinition definition) {
		super(genome, definition);
		if (definition.lockedActivationFunction() != null) {
			activationFunction = definition.lockedActivationFunction();
		}
		else {
			activationFunction = jeat().defaultActivationFunction();
		}
		if (definition.lockedAggregationFunction() != null) {
			aggregationFunction = definition.lockedAggregationFunction();
		}
		else {
			aggregationFunction = jeat().defaultAggregationFunction();
		}
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
			aggregationFunction =
				jeat().aggregationFunctions().getRandomElement(rnd, aggregationFunction);
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
	
	@Override
	public double distance(@NotNull NodeGene<?, ?> other) {
		if (!(other instanceof WorkingNodeGene)) {
			switch (GlobalSettings.COMPARING_DIFFERENT_NODE_TYPES) {
				case THROW:
					throw new IllegalArgumentException(
						"Cannot compare different node types. (%s, %s)".formatted(getClass().getName(),
							other.getClass().getName()));
				case WARN:
					System.err.printf("(JEAT) [WARN] Cannot compare different node types. (%s, %s)%n",
						getClass().getName(), other.getClass().getName());
				case IGNORE:
					return 0.0;
			}
		}
		WorkingNodeGene o = (WorkingNodeGene) other;
		double distance = 0.0;
		if (enabled != o.enabled) return jeat().config().distance.node.disjointCoefficient;
		if (!enabled) return 0.0;
		if (!activationFunction.equals(o.activationFunction)) {
			distance += jeat().config().distance.node.activationFunctionCoefficient;
		}
		if (!aggregationFunction.equals(o.aggregationFunction)) {
			distance += jeat().config().distance.node.aggregationFunctionCoefficient;
		}
		return distance;
	}
	
}
