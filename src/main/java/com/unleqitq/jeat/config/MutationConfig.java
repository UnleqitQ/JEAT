package com.unleqitq.jeat.config;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public class MutationConfig {
	
	/**
	 * The mutation configuration for nodes.
	 */
	@NotNull
	@Builder.Default
	public NodeMutationConfig node = NodeMutationConfig.builder().build();
	/**
	 * The mutation configuration for connections.
	 */
	@NotNull
	@Builder.Default
	public ConnectionMutationConfig connection = ConnectionMutationConfig.builder().build();
	
	
	@Builder
	public static class NodeMutationConfig {
		
		/**
		 * The chance of mutating the structure of a genome (adding, removing or toggling nodes).
		 */
		@NotNull
		@Builder.Default
		public NodeStructureMutationConfig structure = NodeStructureMutationConfig.builder().build();
		/**
		 * The chance of mutating the aggregation function of a node.
		 */
		@NotNull
		@Builder.Default
		public AggregationMutationConfig aggregation = AggregationMutationConfig.builder().build();
		/**
		 * The chance of mutating the activation function of a node.
		 */
		@NotNull
		@Builder.Default
		public ActivationMutationConfig activation = ActivationMutationConfig.builder().build();
		
		/**
		 * The configuration for mutating the response of a node.
		 */
		@NotNull
		@Builder.Default
		public ResponseMutationConfig response = ResponseMutationConfig.builder().build();
		
		@Builder
		public static class NodeStructureMutationConfig {
			
			/**
			 * The chance of adding a node in different ways.
			 */
			@NotNull
			@Builder.Default
			public AddNodeMutationConfig add = AddNodeMutationConfig.builder().build();
			
			/**
			 * The chance of removing a node.
			 */
			@Builder.Default
			public double removeNodeChance = 0.01;
			/**
			 * The chance of toggling a node.
			 */
			@Builder.Default
			public double toggleNodeChance = 0.05;
			
			@Builder
			public static class AddNodeMutationConfig {
				
				/**
				 * The chance of adding a node by splitting a connection.
				 */
				@Builder.Default
				public double splitChance = 0.03;
				/**
				 * The chance of adding a node by taking all the inputs of a node and adding a new node with
				 * those inputs and creating a connection between the new node and the original node.
				 */
				@Builder.Default
				public double combineInputsChance = 0.08;
				/**
				 * The chance of adding a node by taking all the outputs of a node and adding a new node with
				 * those outputs and creating a connection between the original node and the new node.
				 */
				@Builder.Default
				public double combineOutputsChance = 0.08;
				
			}
			
		}
		
		@Builder
		public static class AggregationMutationConfig {
			
			@Builder.Default
			public double changeAggregationFunctionChance = 0.01;
			
		}
		
		@Builder
		public static class ActivationMutationConfig {
			
			@Builder.Default
			public double changeActivationFunctionChance = 0.01;
			
		}
		
		/**
		 * The configuration for mutating the response of a node.
		 */
		@Builder
		public static class ResponseMutationConfig {
			
			/**
			 * The chance of mutating the response of a node.
			 */
			@Builder.Default
			public double mutateResponseChance = 0.3;
			
			/**
			 * The range of the amount of response that can be added or subtracted from a node.
			 */
			@Builder.Default
			public double mutateResponseRange = 0.1;
			
			/**
			 * Whether the response of a node has bounds.
			 */
			@Builder.Default
			public boolean hasBounds = true;
			
			/**
			 * The bounds of the response of a node (same value for positive and negative bounds).
			 */
			@Builder.Default
			public double bounds = 2.0;
			
		}
		
	}
	
	@Builder
	public static class ConnectionMutationConfig {
		
		/**
		 * The chance of mutating the structure of a genome (adding, removing or toggling connections).
		 */
		@NotNull
		@Builder.Default
		public ConnectionStructureMutationConfig structure =
			ConnectionStructureMutationConfig.builder().build();
		/**
		 * The chance of mutating the weight of a connection.
		 */
		@NotNull
		@Builder.Default
		public WeightMutationConfig weight = WeightMutationConfig.builder().build();
		
		@Builder
		public static class ConnectionStructureMutationConfig {
			
			/**
			 * The chance of adding a connection.
			 */
			@Builder.Default
			public double addConnectionChance = 0.08;
			/**
			 * The number of attempts to add a connection.
			 */
			@Builder.Default
			public int addConnectionAttempts = 100;
			/**
			 * The chance of removing a connection.
			 */
			@Builder.Default
			public double removeConnectionChance = 0.03;
			/**
			 * The chance of toggling a connection.
			 */
			@Builder.Default
			public double toggleConnectionChance = 0.05;
			/**
			 * The range of the weight of a new connection.
			 */
			@Builder.Default
			public double newConnectionWeightRange = 1.0;
			
		}
		
		@Builder
		public static class WeightMutationConfig {
			
			/**
			 * The chance of mutating the weight of a connection.
			 */
			@Builder.Default
			public double mutateWeightChance = 0.3;
			/**
			 * The range of the amount of weight that can be added or subtracted from a connection.
			 */
			@Builder.Default
			public double mutateWeightRange = 0.1;
			/**
			 * The chance of completely changing the weight of a connection.
			 */
			@Builder.Default
			public double randomWeightChance = 0.04;
			/**
			 * The range of the amount of weight that can be randomly assigned to a connection.
			 */
			@Builder.Default
			public double randomWeightRange = 1.0;
			
		}
		
		@Builder
		public static class WeightBoundsConfig {
			
			/**
			 * Whether the weight of a connection has bounds.
			 */
			@Builder.Default
			public boolean hasBounds = true;
			/**
			 * The bounds of the weight of a connection (same value for positive and negative bounds).
			 */
			@Builder.Default
			public double bounds = 1.0;
			
		}
		
	}
	
}
