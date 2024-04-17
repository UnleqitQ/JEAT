package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.StagnationConfig;
import com.unleqitq.jeat.genetics.genome.Genome;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.function.ToDoubleFunction;

@XmlRootElement (name = "stagnation")
@XmlType (propOrder = {})
public class XmlStagnationConfig {
	
	// the fitness function is provided as an additional parameter in java code
	
	@XmlElement (name = "max-stagnation", required = true)
	public int maxStagnation = 15;
	
	@XmlElement (name = "species-elitism", required = true)
	public int speciesElitism = 3;
	
	@XmlElement (name = "improvement-threshold", required = true)
	public double improvementThreshold = 0.01;
	
	public static XmlStagnationConfig of(StagnationConfig config) {
		XmlStagnationConfig xmlConfig = new XmlStagnationConfig();
		xmlConfig.maxStagnation = config.maxStagnation;
		xmlConfig.speciesElitism = config.speciesElitism;
		xmlConfig.improvementThreshold = config.improvementThreshold;
		return xmlConfig;
	}
	
	public StagnationConfig to(ToDoubleFunction<Collection<Genome>> fitnessFunction) {
		return StagnationConfig.builder()
			.maxStagnation(maxStagnation)
			.speciesElitism(speciesElitism)
			.improvementThreshold(improvementThreshold)
			.fitnessFunction(fitnessFunction)
			.build();
	}
	
}
