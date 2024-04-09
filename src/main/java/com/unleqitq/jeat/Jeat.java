package com.unleqitq.jeat;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.activationFunction.functions.SigmoidActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.aggregationFunction.functions.SumAggregationFunction;
import com.unleqitq.jeat.config.JeatConfig;
import com.unleqitq.jeat.genetics.genome.GenomeStore;
import com.unleqitq.jeat.jeat.ConnectionDefinitionStore;
import com.unleqitq.jeat.jeat.GenomeHelper;
import com.unleqitq.jeat.jeat.NodeDefinitionStore;
import com.unleqitq.jeat.utils.RandomElementList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Main class of the Java NEAT Port.
 *
 * @since 1.0
 */
@Accessors (fluent = true)
@Getter
public class Jeat {
	
	/**
	 * The default activation function.
	 */
	@Setter
	@NotNull
	private ActivationFunction defaultActivationFunction = new SigmoidActivationFunction();
	/**
	 * The default aggregation function.
	 */
	@Setter
	@NotNull
	private AggregationFunction defaultAggregationFunction = new SumAggregationFunction();
	/**
	 * The activation functions that can be used in the genomes.
	 */
	@NotNull
	private final RandomElementList<ActivationFunction> activationFunctions =
		new RandomElementList<>();
	/**
	 * The aggregation functions that can be used in the genomes.
	 */
	@NotNull
	private final RandomElementList<AggregationFunction> aggregationFunctions =
		new RandomElementList<>();
	
	/**
	 * The configuration of this instance of JEAT, containing all the settings.
	 */
	@NotNull
	private final JeatConfig config;
	
	/**
	 * The random instance used by JEAT.
	 */
	@NotNull
	private final Random random = new Random();
	
	/**
	 * All the node definitions that are being used in the genomes or were used in the past.
	 */
	@NotNull
	private final NodeDefinitionStore nodeDefinitions = new NodeDefinitionStore();
	/**
	 * All the connection definitions that are being used in the genomes or were used in the past.
	 */
	@NotNull
	private final ConnectionDefinitionStore connectionDefinitions = new ConnectionDefinitionStore();
	/**
	 * The store that contains all the genomes, that are currently managed by JEAT,
	 * but you can also add genomes to this store manually or remove them etc.
	 */
	@NotNull
	private final GenomeStore genomeStore = new GenomeStore();
	
	// Helpers
	/**
	 * The helper that contains methods which have to do with genomes.
	 */
	@NotNull
	private final GenomeHelper genomeHelper = new GenomeHelper(this);
	
	/**
	 * Creates a new instance of JEAT with the given configuration.
	 *
	 * @param config The configuration of this instance of JEAT.
	 */
	public Jeat(@NotNull JeatConfig config) {
		this.config = config;
	}
	
}
