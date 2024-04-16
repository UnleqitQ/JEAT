package com.unleqitq.jeat.config.loader.xml;


import com.unleqitq.jeat.config.DistanceConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "distance")
@XmlType
public class XmlDistanceConfig {
	
	@XmlElement (name = "node", required = true)
	public XmlNodeDistanceConfig node;
	
	@XmlElement (name = "connection", required = true)
	public XmlConnectionDistanceConfig connection;
	
	@XmlType
	public static class XmlNodeDistanceConfig {
		
		@XmlElement (name = "disjoint", required = true)
		public double disjointCoefficient = 1.0;
		
		@XmlElement (name = "activation", required = true)
		public double activationFunctionCoefficient = 0.2;
		
		@XmlElement (name = "activation-parameter", required = true)
		public double activationFunctionParameterCoefficient = 0.05;
		
		@XmlElement (name = "aggregation", required = true)
		public double aggregationFunctionCoefficient = 0.2;
		
		@XmlElement (name = "response", required = true)
		public double responseCoefficient = 0.1;
		
		@XmlElement (name = "max-response-difference", required = true)
		public double maxResponseDifference = 1.0;
		
	}
	
	@XmlType
	public static class XmlConnectionDistanceConfig {
		
		@XmlElement (name = "disjoint", required = true)
		public double disjointCoefficient = 1.0;
		
		@XmlElement (name = "weight", required = true)
		public double weightCoefficient = 0.5;
		
		@XmlElement (name = "max-weight-difference", required = true)
		public double maxWeightDifference = 1.0;
		
	}
	
	public static XmlDistanceConfig of(DistanceConfig config) {
		XmlDistanceConfig xmlConfig = new XmlDistanceConfig();
		xmlConfig.node = new XmlNodeDistanceConfig();
		xmlConfig.node.disjointCoefficient = config.node.disjointCoefficient;
		xmlConfig.node.activationFunctionCoefficient = config.node.activationFunctionCoefficient;
		xmlConfig.node.activationFunctionParameterCoefficient =
			config.node.activationFunctionParameterCoefficient;
		xmlConfig.node.aggregationFunctionCoefficient = config.node.aggregationFunctionCoefficient;
		xmlConfig.node.responseCoefficient = config.node.responseCoefficient;
		xmlConfig.node.maxResponseDifference = config.node.maxResponseDifference;
		xmlConfig.connection = new XmlConnectionDistanceConfig();
		xmlConfig.connection.disjointCoefficient = config.connection.disjointCoefficient;
		xmlConfig.connection.weightCoefficient = config.connection.weightCoefficient;
		xmlConfig.connection.maxWeightDifference = config.connection.maxWeightDifference;
		return xmlConfig;
	}
	
	public DistanceConfig to() {
		return DistanceConfig.builder()
			.node(DistanceConfig.NodeDistanceConfig.builder()
				.disjointCoefficient(node.disjointCoefficient)
				.activationFunctionCoefficient(node.activationFunctionCoefficient)
				.activationFunctionParameterCoefficient(node.activationFunctionParameterCoefficient)
				.aggregationFunctionCoefficient(node.aggregationFunctionCoefficient)
				.responseCoefficient(node.responseCoefficient)
				.maxResponseDifference(node.maxResponseDifference)
				.build())
			.connection(DistanceConfig.ConnectionDistanceConfig.builder()
				.disjointCoefficient(connection.disjointCoefficient)
				.weightCoefficient(connection.weightCoefficient)
				.maxWeightDifference(connection.maxWeightDifference)
				.build())
			.build();
	}
	
}
