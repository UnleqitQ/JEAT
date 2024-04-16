package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.CrossoverConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "crossover")
@XmlType
public class XmlCrossoverConfig {
	
	@XmlElement (name = "geneInheritanceProbability", required = true)
	public double geneInheritanceProbability = 0.5;
	
	@XmlElement (name = "thinOutConnections", required = true)
	public boolean thinOutConnections = true;
	
	@XmlElement (name = "thinOutPercentage", required = true)
	public double thinOutPercentage = 0.2;
	
	public CrossoverConfig to() {
		return CrossoverConfig.builder()
			.geneInheritanceProbability(geneInheritanceProbability)
			.thinOutConnections(thinOutConnections)
			.thinOutPercentage(thinOutPercentage)
			.build();
	}
	
	public static XmlCrossoverConfig of(CrossoverConfig config) {
		XmlCrossoverConfig xmlConfig = new XmlCrossoverConfig();
		xmlConfig.geneInheritanceProbability = config.geneInheritanceProbability;
		xmlConfig.thinOutConnections = config.thinOutConnections;
		xmlConfig.thinOutPercentage = config.thinOutPercentage;
		return xmlConfig;
	}
	
}
