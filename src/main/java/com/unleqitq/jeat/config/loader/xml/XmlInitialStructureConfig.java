package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.InitialStructureConfig;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@XmlRootElement
@XmlType (propOrder = {})
public class XmlInitialStructureConfig {
	
	@XmlElement (name = "input-node", required = true)
	@XmlElementWrapper (name = "input-nodes", required = true)
	public List<XmlInputNodeConfig> inputNodes;
	
	@XmlElement (name = "output-node", required = true)
	@XmlElementWrapper (name = "output-nodes", required = true)
	public List<XmlOutputNodeConfig> outputNodes;
	
	@XmlElement (name = "connection-density", required = true)
	public double connectionDensity = 0.5;
	
	@XmlType (propOrder = {})
	public static class XmlInputNodeConfig {
		
		@XmlElement (name = "name", required = true)
		public String name;
		
		@XmlElement (name = "x")
		public double x = 0;
		
	}
	
	@XmlType (propOrder = {})
	public static class XmlOutputNodeConfig {
		
		@XmlElement (name = "name", required = true)
		public String name;
		
		@XmlElement (name = "x", required = false)
		public double x = 1;
		
		@XmlElement (name = "activation-function", required = false)
		public String activationFunction = null;
		
		@XmlElement (name = "aggregation-function", required = false)
		public String aggregationFunction = null;
		
	}
	
	public static XmlInitialStructureConfig of(InitialStructureConfig config,
		Function<ActivationFunction, String> activationFunctionMapper,
		Function<AggregationFunction, String> aggregationFunctionMapper) {
		XmlInitialStructureConfig xmlConfig = new XmlInitialStructureConfig();
		xmlConfig.inputNodes = config.inputNodes.stream().map(inputNode -> {
			XmlInputNodeConfig xmlInputNode = new XmlInputNodeConfig();
			xmlInputNode.name = inputNode.name;
			xmlInputNode.x = inputNode.x;
			return xmlInputNode;
		}).toList();
		xmlConfig.outputNodes = config.outputNodes.stream().map(outputNode -> {
			XmlOutputNodeConfig xmlOutputNode = new XmlOutputNodeConfig();
			xmlOutputNode.name = outputNode.name;
			xmlOutputNode.x = outputNode.x;
			if (outputNode.lockedActivationFunction != null) xmlOutputNode.activationFunction =
				activationFunctionMapper.apply(outputNode.lockedActivationFunction);
			if (outputNode.lockedAggregationFunction != null) xmlOutputNode.aggregationFunction =
				aggregationFunctionMapper.apply(outputNode.lockedAggregationFunction);
			return xmlOutputNode;
		}).toList();
		xmlConfig.connectionDensity = config.connectionDensity;
		return xmlConfig;
	}
	
	public InitialStructureConfig to(@NotNull Map<String, ActivationFunction> activationFunctions,
		@NotNull Map<String, AggregationFunction> aggregationFunctions) {
		if (inputNodes == null || outputNodes == null) {
			throw new IllegalArgumentException("Input nodes and output nodes must be provided.");
		}
		return InitialStructureConfig.builder()
			.inputNodes(inputNodes.stream()
				.map(inputNode -> InitialStructureConfig.InputNodeConfig.builder()
					.name(inputNode.name)
					.x(inputNode.x)
					.build())
				.toList())
			.outputNodes(outputNodes.stream().map(outputNode -> {
				InitialStructureConfig.OutputNodeConfig.OutputNodeConfigBuilder builder =
					InitialStructureConfig.OutputNodeConfig.builder().name(outputNode.name).x(outputNode.x);
				if (outputNode.activationFunction != null)
					builder.lockedActivationFunction(activationFunctions.get(outputNode.activationFunction));
				if (outputNode.aggregationFunction != null) builder.lockedAggregationFunction(
					aggregationFunctions.get(outputNode.aggregationFunction));
				return builder.build();
			}).toList())
			.connectionDensity(connectionDensity)
			.build();
	}
	
}
