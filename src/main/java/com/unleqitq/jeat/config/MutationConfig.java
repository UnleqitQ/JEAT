package com.unleqitq.jeat.config;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public class MutationConfig {
	
	/**
	 * The mutation configuration for nodes.
	 */
	@NotNull
	public NodeMutationConfig node;
	/**
	 * The mutation configuration for connections.
	 */
	@NotNull
	public ConnectionMutationConfig connection;
	
	
	@Builder
	public static class NodeMutationConfig {
		
		/**
		 * The chance of mutating the structure of a genome (adding, removing or toggling nodes).
		 */
		@NotNull
		public NodeStructureMutationConfig nodeStructure;
		/**
		 * The chance of mutating the aggregation function of a node.
		 */
		@NotNull
		public AggregationMutationConfig aggregation;
		/**
		 * The chance of mutating the activation function of a node.
		 */
		@NotNull
		public ActivationMutationConfig activation;
		
		@Builder
		public static class NodeStructureMutationConfig {
			
			/**
			 * The chance of adding a node in different ways.
			 */
			@NotNull
			public AddNodeMutationConfig addNode;
			
			/**
			 * The chance of removing a node.
			 */
			public double removeNodeChance = 0.01;
			/**
			 * The chance of toggling a node.
			 */
			public double toggleNodeChance = 0.05;
			
			@Builder
			public static class AddNodeMutationConfig {
				
				/**
				 * The chance of adding a node by splitting a connection.
				 */
				public double splitChance = 0.03;
				/**
				 * The chance of adding a node by taking all the inputs of a node and adding a new node with
				 * those inputs and creating a connection between the new node and the original node.
				 */
				public double combineInputsChance = 0.08;
				/**
				 * The chance of adding a node by taking all the outputs of a node and adding a new node with
				 * those outputs and creating a connection between the original node and the new node.
				 */
				public double combineOutputsChance = 0.08;
				
			}
			
		}
		
		@Builder
		public static class AggregationMutationConfig {
			
			public double changeAggregationFunctionChance = 0.01;
			
		}
		
		@Builder
		public static class ActivationMutationConfig {
			
			public double changeActivationFunctionChance = 0.01;
			
		}
		
	}
	
	@Builder
	public static class ConnectionMutationConfig {
		
		/**
		 * The chance of mutating the structure of a genome (adding, removing or toggling connections).
		 */
		@NotNull
		public ConnectionStructureMutationConfig connectionStructure;
		/**
		 * The chance of mutating the weight of a connection.
		 */
		@NotNull
		public WeightMutationConfig weight;
		
		@Builder
		public static class ConnectionStructureMutationConfig {
			
			/**
			 * The chance of adding a connection.
			 */
			public double addConnectionChance = 0.08;
			/**
			 * The chance of removing a connection.
			 */
			public double removeConnectionChance = 0.03;
			/**
			 * The chance of toggling a connection.
			 */
			public double toggleConnectionChance = 0.05;
			/**
			 * The range of the weight of a new connection.
			 */
			public double newConnectionWeightRange = 1.0;
			
		}
		
		@Builder
		public static class WeightMutationConfig {
			
			/**
			 * The chance of mutating the weight of a connection.
			 */
			public double mutateWeightChance = 0.3;
			/**
			 * The range of the amount of weight that can be added or subtracted from a connection.
			 */
			public double mutateWeightRange = 0.1;
			/**
			 * The chance of completely changing the weight of a connection.
			 */
			public double randomWeightChance = 0.04;
			/**
			 * The range of the amount of weight that can be randomly assigned to a connection.
			 */
			public double randomWeightRange = 1.0;
			
		}
		
		@Builder
		public static class WeightBoundsConfig {
			
			/**
			 * Whether the weight of a connection has bounds.
			 */
			public boolean hasBounds = true;
			/**
			 * The bounds of the weight of a connection (same value for positive and negative bounds).
			 */
			public double bounds = 1.0;
			
		}
		
	}
	
}
