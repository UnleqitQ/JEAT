package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.JeatConfig;
import com.unleqitq.jeat.genetics.genome.Genome;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

@XmlRootElement (name = "jeat")
@XmlType
public class XmlJeatConfig {
	
	@XmlElement (name = "mutation")
	public XmlMutationConfig mutation;
	
	@XmlElement (name = "initial-structure")
	public XmlInitialStructureConfig initialStructure;
	
	@XmlElement (name = "crossover")
	public XmlCrossoverConfig crossover;
	
	@XmlElement (name = "species")
	public XmlSpeciesConfig species;
	
	@XmlElement (name = "distance")
	public XmlDistanceConfig distance;
	
	@XmlElement (name = "stagnation")
	public XmlStagnationConfig stagnation;
	
	@NotNull
	public static XmlJeatConfig of(@NotNull JeatConfig config,
		@NotNull Function<ActivationFunction, String> activationFunctionNameMapper,
		@NotNull Function<AggregationFunction, String> aggregationFunctionNameMapper) {
		XmlJeatConfig xmlConfig = new XmlJeatConfig();
		xmlConfig.mutation = XmlMutationConfig.of(config.mutation);
		xmlConfig.initialStructure =
			XmlInitialStructureConfig.of(config.initialStructure, activationFunctionNameMapper,
				aggregationFunctionNameMapper);
		xmlConfig.crossover = XmlCrossoverConfig.of(config.crossover);
		xmlConfig.species = XmlSpeciesConfig.of(config.species);
		xmlConfig.distance = XmlDistanceConfig.of(config.distance);
		xmlConfig.stagnation = XmlStagnationConfig.of(config.stagnation);
		return xmlConfig;
	}
	
	@NotNull
	public JeatConfig to(@NotNull Map<String, ActivationFunction> activationFunctions,
		@NotNull Map<String, AggregationFunction> aggregationFunctions,
		@NotNull ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction) {
		return JeatConfig.builder()
			.mutation(mutation.to())
			.initialStructure(initialStructure.to(activationFunctions, aggregationFunctions))
			.crossover(crossover.to())
			.species(species.to())
			.distance(distance.to())
			.stagnation(stagnation.to(stagnationFitnessFunction))
			.build();
	}
	
}
