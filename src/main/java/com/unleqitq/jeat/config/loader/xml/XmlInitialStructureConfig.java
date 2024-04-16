package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.InitialStructureConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;

@XmlRootElement
@XmlType
public class XmlInitialStructureConfig {
	
	@XmlElement (name = "input-nodes", required = true)
	public XmlInputNodeConfig[] inputNodes;
	
	@XmlElement (name = "output-nodes", required = true)
	public XmlOutputNodeConfig[] outputNodes;
	
	@XmlElement (name = "connection-density", required = true)
	public double connectionDensity = 0.5;
	
	@XmlType
	public static class XmlInputNodeConfig {
		
		@XmlElement (name = "name", required = true)
		public String name;
		
		@XmlElement (name = "x")
		public double x = 0;
		
	}
	
	@XmlType
	public static class XmlOutputNodeConfig {
		
		@XmlElement (name = "name", required = true)
		public String name;
		
		@XmlElement (name = "x")
		public double x = 1;
		
		@XmlElement (name = "can-disable")
		public boolean canDisable = false;
		
	}
	
	public static XmlInitialStructureConfig of(InitialStructureConfig config) {
		XmlInitialStructureConfig xmlConfig = new XmlInitialStructureConfig();
		xmlConfig.inputNodes = config.inputNodes.stream().map(inputNode -> {
			XmlInputNodeConfig xmlInputNode = new XmlInputNodeConfig();
			xmlInputNode.name = inputNode.name;
			xmlInputNode.x = inputNode.x;
			return xmlInputNode;
		}).toArray(XmlInputNodeConfig[]::new);
		xmlConfig.outputNodes = config.outputNodes.stream().map(outputNode -> {
			XmlOutputNodeConfig xmlOutputNode = new XmlOutputNodeConfig();
			xmlOutputNode.name = outputNode.name;
			xmlOutputNode.x = outputNode.x;
			xmlOutputNode.canDisable = outputNode.canDisable;
			return xmlOutputNode;
		}).toArray(XmlOutputNodeConfig[]::new);
		xmlConfig.connectionDensity = config.connectionDensity;
		return xmlConfig;
	}
	
	public InitialStructureConfig to() {
		if (inputNodes == null || outputNodes == null) {
			throw new IllegalArgumentException("Input nodes and output nodes must be provided.");
		}
		return InitialStructureConfig.builder()
			.inputNodes(Arrays.stream(inputNodes)
				.map(inputNode -> InitialStructureConfig.InputNodeConfig.builder()
					.name(inputNode.name)
					.x(inputNode.x)
					.build())
				.toList())
			.outputNodes(Arrays.stream(outputNodes)
				.map(outputNode -> InitialStructureConfig.OutputNodeConfig.builder()
					.name(outputNode.name)
					.x(outputNode.x)
					.canDisable(outputNode.canDisable)
					.build())
				.toList())
			.connectionDensity(connectionDensity)
			.build();
	}
	
}
