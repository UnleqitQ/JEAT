package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.MutationConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "mutation")
@XmlType (propOrder = {})
public class XmlMutationConfig {
	
	@XmlElement (name = "node", required = true)
	public XmlNodeMutationConfig node;
	
	@XmlElement (name = "connection", required = true)
	public XmlConnectionMutationConfig connection;
	
	@XmlType (propOrder = {})
	public static class XmlNodeMutationConfig {
		
		@XmlElement (name = "structure", required = true)
		public XmlNodeStructureMutationConfig structure;
		
		@XmlElement (name = "aggregation", required = true)
		public XmlAggregationMutationConfig aggregation;
		
		@XmlElement (name = "activation", required = true)
		public XmlActivationMutationConfig activation;
		
		@XmlElement (name = "response", required = true)
		public XmlResponseMutationConfig response;
		
		@XmlType (propOrder = {})
		public static class XmlNodeStructureMutationConfig {
			
			@XmlElement (name = "add", required = true)
			public XmlAddNodeMutationConfig add;
			
			@XmlElement (name = "remove", required = true)
			public double removeNodeChance;
			
			@XmlType (propOrder = {})
			public static class XmlAddNodeMutationConfig {
				
				@XmlElement (name = "split", required = true)
				public double splitChance;
				
				@XmlElement (name = "combine-inputs", required = true)
				public double combineInputsChance;
				
				@XmlElement (name = "combine-outputs", required = true)
				public double combineOutputsChance;
				
			}
			
		}
		
		@XmlType (propOrder = {})
		public static class XmlAggregationMutationConfig {
			
			@XmlElement (name = "change", required = true)
			public double changeAggregationFunctionChance;
			
		}
		
		@XmlType (propOrder = {})
		public static class XmlActivationMutationConfig {
			
			@XmlElement (name = "change", required = true)
			public double changeActivationFunctionChance;
			
			@XmlElement (name = "change-parameters", required = true)
			public double changeActivationFunctionParametersChance;
			
		}
		
		@XmlType (propOrder = {})
		public static class XmlResponseMutationConfig {
			
			@XmlElement (name = "chance", required = true)
			public double mutateResponseChance;
			
			@XmlElement (name = "range", required = true)
			public double mutateResponseRange;
			
			@XmlElement (name = "has-bounds", required = true)
			public boolean hasBounds;
			
			@XmlElement (name = "bounds", required = true)
			public double bounds;
			
		}
		
	}
	
	@XmlType (propOrder = {})
	public static class XmlConnectionMutationConfig {
		
		@XmlElement (name = "structure", required = true)
		public XmlConnectionStructureMutationConfig structure;
		
		@XmlElement (name = "weight", required = true)
		public XmlConnectionWeightMutationConfig weight;
		
		@XmlType (propOrder = {})
		public static class XmlConnectionStructureMutationConfig {
			
			@XmlElement (name = "add", required = true)
			public double addConnectionChance;
			
			@XmlElement (name = "remove", required = true)
			public double removeConnectionChance;
			
			@XmlElement (name = "toggle", required = true)
			public double toggleConnectionChance;
			
		}
		
		@XmlType (propOrder = {})
		public static class XmlConnectionWeightMutationConfig {
			
			@XmlElement (name = "chance", required = true)
			public double mutateWeightChance;
			
			@XmlElement (name = "range", required = true)
			public double mutateWeightRange;
			
			@XmlElement (name = "random-chance", required = true)
			public double randomWeightChance;
			
			@XmlElement (name = "random-range", required = true)
			public double randomWeightRange;
			
		}
		
	}
	
	public static XmlMutationConfig of(MutationConfig config) {
		XmlMutationConfig xmlConfig = new XmlMutationConfig();
		xmlConfig.node = new XmlNodeMutationConfig();
		xmlConfig.node.structure = new XmlNodeMutationConfig.XmlNodeStructureMutationConfig();
		xmlConfig.node.structure.add =
			new XmlNodeMutationConfig.XmlNodeStructureMutationConfig.XmlAddNodeMutationConfig();
		xmlConfig.node.aggregation = new XmlNodeMutationConfig.XmlAggregationMutationConfig();
		xmlConfig.node.activation = new XmlNodeMutationConfig.XmlActivationMutationConfig();
		xmlConfig.node.response = new XmlNodeMutationConfig.XmlResponseMutationConfig();
		xmlConfig.connection = new XmlConnectionMutationConfig();
		xmlConfig.connection.structure =
			new XmlConnectionMutationConfig.XmlConnectionStructureMutationConfig();
		xmlConfig.connection.weight =
			new XmlConnectionMutationConfig.XmlConnectionWeightMutationConfig();
		
		xmlConfig.node.structure.add.splitChance = config.node.structure.add.splitChance;
		xmlConfig.node.structure.add.combineInputsChance =
			config.node.structure.add.combineInputsChance;
		xmlConfig.node.structure.add.combineOutputsChance =
			config.node.structure.add.combineOutputsChance;
		xmlConfig.node.structure.removeNodeChance = config.node.structure.removeNodeChance;
		xmlConfig.node.aggregation.changeAggregationFunctionChance =
			config.node.aggregation.changeAggregationFunctionChance;
		xmlConfig.node.activation.changeActivationFunctionChance =
			config.node.activation.changeActivationFunctionChance;
		xmlConfig.node.activation.changeActivationFunctionParametersChance =
			config.node.activation.changeActivationFunctionParametersChance;
		xmlConfig.node.response.mutateResponseChance = config.node.response.mutateResponseChance;
		xmlConfig.node.response.mutateResponseRange = config.node.response.mutateResponseRange;
		xmlConfig.node.response.hasBounds = config.node.response.hasBounds;
		xmlConfig.node.response.bounds = config.node.response.bounds;
		xmlConfig.connection.structure.addConnectionChance =
			config.connection.structure.addConnectionChance;
		xmlConfig.connection.structure.removeConnectionChance =
			config.connection.structure.removeConnectionChance;
		xmlConfig.connection.structure.toggleConnectionChance =
			config.connection.structure.toggleConnectionChance;
		xmlConfig.connection.weight.mutateWeightChance = config.connection.weight.mutateWeightChance;
		xmlConfig.connection.weight.mutateWeightRange = config.connection.weight.mutateWeightRange;
		xmlConfig.connection.weight.randomWeightChance = config.connection.weight.randomWeightChance;
		xmlConfig.connection.weight.randomWeightRange = config.connection.weight.randomWeightRange;
		
		return xmlConfig;
	}
	
	public MutationConfig to() {
		return MutationConfig.builder()
			.node(MutationConfig.NodeMutationConfig.builder().build())
			.connection(MutationConfig.ConnectionMutationConfig.builder()
				.weight(MutationConfig.ConnectionMutationConfig.WeightMutationConfig.builder()
					.mutateWeightChance(connection.weight.mutateWeightChance)
					.mutateWeightRange(connection.weight.mutateWeightRange)
					.randomWeightChance(connection.weight.randomWeightChance)
					.randomWeightRange(connection.weight.randomWeightRange)
					.build())
				.structure(
					MutationConfig.ConnectionMutationConfig.ConnectionStructureMutationConfig.builder()
						.addConnectionChance(connection.structure.addConnectionChance)
						.removeConnectionChance(connection.structure.removeConnectionChance)
						.toggleConnectionChance(connection.structure.toggleConnectionChance)
						.build())
				.build())
			.node(MutationConfig.NodeMutationConfig.builder()
				.structure(MutationConfig.NodeMutationConfig.NodeStructureMutationConfig.builder()
					.add(
						MutationConfig.NodeMutationConfig.NodeStructureMutationConfig.AddNodeMutationConfig.builder()
							.splitChance(node.structure.add.splitChance)
							.combineInputsChance(node.structure.add.combineInputsChance)
							.combineOutputsChance(node.structure.add.combineOutputsChance)
							.build())
					.removeNodeChance(node.structure.removeNodeChance)
					.build())
				.aggregation(MutationConfig.NodeMutationConfig.AggregationMutationConfig.builder()
					.changeAggregationFunctionChance(node.aggregation.changeAggregationFunctionChance)
					.build())
				.activation(MutationConfig.NodeMutationConfig.ActivationMutationConfig.builder()
					.changeActivationFunctionChance(node.activation.changeActivationFunctionChance)
					.changeActivationFunctionParametersChance(
						node.activation.changeActivationFunctionParametersChance)
					.build())
				.response(MutationConfig.NodeMutationConfig.ResponseMutationConfig.builder()
					.mutateResponseChance(node.response.mutateResponseChance)
					.mutateResponseRange(node.response.mutateResponseRange)
					.hasBounds(node.response.hasBounds)
					.bounds(node.response.bounds)
					.build())
				.build())
			.build();
	}
	
}
