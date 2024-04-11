package com.unleqitq.jeat.config;

import lombok.Builder;

/**
 * Configuration for the distance calculation between two genomes.
 */
@Builder
public class DistanceConfig {
	
	/**
	 * The configuration used for the distance calculation between two nodes.
	 */
	public NodeDistanceConfig node;
	
	/**
	 * The configuration used for the distance calculation between two connections.
	 */
	public ConnectionDistanceConfig connection;
	
	/**
	 * Configuration for the distance calculation between two nodes.
	 */
	@Builder
	public static class NodeDistanceConfig {
		
		/**
		 * The distance used for nodes that are not present in both genomes.
		 */
		@Builder.Default
		public double disjointCoefficient = 1.0;
		
		/**
		 * The distance used for different activation functions.
		 */
		@Builder.Default
		public double activationFunctionCoefficient = 0.2;
		
		/**
		 * The distance used for different aggregation functions.
		 */
		@Builder.Default
		public double aggregationFunctionCoefficient = 0.2;
		
		/**
		 * The factor that the response difference is multiplied with.
		 */
		@Builder.Default
		public double responseCoefficient = 0.1;
		
		/**
		 * The maximum response difference to be used in the distance calculation. If the difference is greater, this value is used instead.
		 */
		@Builder.Default
		public double maxResponseDifference = 5.0;
		
	}
	
	/**
	 * Configuration for the distance calculation between two connections.
	 */
	@Builder
	public static class ConnectionDistanceConfig {
		
		/**
		 * The distance used for connections that are not present in both genomes.
		 */
		@Builder.Default
		public double disjointCoefficient = 1.0;
		
		/**
		 * The distance used for different weights.
		 */
		@Builder.Default
		public double weightCoefficient = 0.4;
		
		/**
		 * The maximum weight difference to be used in the distance calculation. If the difference is greater, this value is used instead.
		 */
		@Builder.Default
		public double maxWeightDifference = 2.0;
		
	}
	
}
