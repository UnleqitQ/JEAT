package com.unleqitq.jeat.genetics.gene.node.working;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.activationFunction.ActivationFunctionReference;
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
	
	private ActivationFunctionReference activationFunction;
	private AggregationFunction aggregationFunction;
	private double response = 1.0;
	
	public WorkingNodeGene(@NotNull Genome genome, @NotNull WorkingNodeGeneDefinition definition) {
		super(genome, definition);
		if (definition.lockedActivationFunction() != null) {
			activationFunction =
				new ActivationFunctionReference(jeat(), definition.lockedActivationFunction());
		}
		else {
			activationFunction =
				new ActivationFunctionReference(jeat(), jeat().defaultActivationFunction());
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
			ActivationFunction newActivationFunction =
				jeat().activationFunctions().getRandomElement(rnd, null);
			if (newActivationFunction != null)
				activationFunction = new ActivationFunctionReference(jeat(), newActivationFunction);
		}
		if (rnd.nextDouble() < config.activation.changeActivationFunctionParametersChance) {
			activationFunction.mutate();
		}
		if (definition().lockedAggregationFunction() == null &&
			rnd.nextDouble() < config.aggregation.changeAggregationFunctionChance) {
			aggregationFunction =
				jeat().aggregationFunctions().getRandomElement(rnd, aggregationFunction);
		}
		if (rnd.nextDouble() < config.response.mutateResponseChance) {
			response +=
				rnd.nextDouble(-config.response.mutateResponseRange, config.response.mutateResponseRange);
			if (config.response.hasBounds) {
				response = Math.min(Math.max(response, -config.response.bounds), config.response.bounds);
			}
		}
	}
	
	@Override
	@NotNull
	public WorkingNodeGene copy(@NotNull Genome genome) {
		WorkingNodeGene copy = new WorkingNodeGene(genome, definition());
		copy.activationFunction = activationFunction.copy();
		copy.aggregationFunction = aggregationFunction;
		copy.response = response;
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
					Jeat.LOGGER.warn("Cannot compare different node types. ({}, {})", getClass().getName(),
						other.getClass().getName());
				case IGNORE:
					return 0.0;
			}
		}
		WorkingNodeGene o = (WorkingNodeGene) other;
		double distance = 0.0;
		if (activationFunction.activationFunction().equals(o.activationFunction.activationFunction())) {
			distance += jeat().config().distance.node.activationFunctionParameterCoefficient *
				activationFunction.activationFunction()
					.distance(activationFunction.parameters().values(),
						o.activationFunction.parameters().values());
		}
		else {
			distance += jeat().config().distance.node.activationFunctionCoefficient;
		}
		if (!aggregationFunction.equals(o.aggregationFunction)) {
			distance += jeat().config().distance.node.aggregationFunctionCoefficient;
		}
		{
			double diff = response - o.response;
			distance += jeat().config().distance.node.responseCoefficient *
				Math.min(Math.abs(diff), jeat().config().distance.node.maxResponseDifference);
		}
		return distance;
	}
	
}
