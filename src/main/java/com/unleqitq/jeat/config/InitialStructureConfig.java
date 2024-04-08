package com.unleqitq.jeat.config;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * The configuration of the initial structure of the genome.<br>
 * Please make sure to provide the names of the input and output nodes as their ids are generated automatically and are random.<br>
 * Also make sure you don't provide the same name for multiple nodes, also the name "bias" is reserved for the bias node.
 */
@Builder
public class InitialStructureConfig {
	
	/**
	 * The configuration of the input nodes.
	 */
	@NotNull
	public Collection<InputNodeConfig> inputNodes;
	
	/**
	 * The configuration of the output nodes.
	 */
	@NotNull
	public Collection<OutputNodeConfig> outputNodes;
	
	/**
	 * The density of the initial connection mesh.
	 */
	@Builder.Default
	public double connectionDensity = 0.5;
	
	@Builder
	public static class InputNodeConfig {
		
		/**
		 * The name of the input node.<br>
		 * You need to provide the names of the input nodes as their ids are generated automatically and are random.
		 */
		@NotNull
		public String name;
		
		/**
		 * The x value of the node. (Default is 0)
		 */
		@Builder.Default
		public double x = 0;
		
	}
	
	@Builder
	public static class OutputNodeConfig {
		
		/**
		 * The name of the output node.<br>
		 * You need to provide the names of the output nodes as their ids are generated automatically and are random.
		 */
		@NotNull
		public String name;
		
		/**
		 * The activation function if you want to lock it.
		 */
		@Nullable
		@Builder.Default
		public ActivationFunction lockedActivationFunction = null;
		
		/**
		 * The aggregation function if you want to lock it.
		 */
		@Nullable
		@Builder.Default
		public AggregationFunction lockedAggregationFunction = null;
		
		/**
		 * If the node can be disabled.<br>
		 * (should be false, but you could do it if you want)
		 */
		@Builder.Default
		public boolean canDisable = false;
		
		/**
		 * The x value of the node. (Default is 1)
		 */
		@Builder.Default
		public double x = 1;
		
	}
	
}
