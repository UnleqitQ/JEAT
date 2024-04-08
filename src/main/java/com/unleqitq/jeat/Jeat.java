package com.unleqitq.jeat;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.JeatConfig;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionGeneDefinition;
import com.unleqitq.jeat.genetics.gene.connection.ConnectionIdentifier;
import com.unleqitq.jeat.genetics.gene.node.NodeGeneDefinition;
import com.unleqitq.jeat.jeat.DefinitionStore;
import com.unleqitq.jeat.utils.RandomElementList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

/**
 * Main class of the Java NEAT Port.
 *
 * @since 1.0
 */
@Accessors (fluent = true)
@Getter
public class Jeat {
	
	@Setter
	@NotNull
	private ActivationFunction defaultActivationFunction = v -> v;
	@Setter
	@NotNull
	private AggregationFunction defaultAggregationFunction =
		vs -> vs.stream().mapToDouble(v -> v).sum();
	@NotNull
	private final RandomElementList<ActivationFunction> activationFunctions =
		new RandomElementList<>();
	@NotNull
	private final RandomElementList<AggregationFunction> aggregationFunctions =
		new RandomElementList<>();
	
	@NotNull
	private final JeatConfig config;
	@NotNull
	private final Random random = new Random();
	
	@NotNull
	private final DefinitionStore<UUID, NodeGeneDefinition<?, ?>> nodeDefinitions =
		new DefinitionStore<>();
	@NotNull
	private final DefinitionStore<ConnectionIdentifier, ConnectionGeneDefinition>
		connectionDefinitions = new DefinitionStore<>();
	
	public Jeat(@NotNull JeatConfig config) {
		this.config = config;
	}
	
}
