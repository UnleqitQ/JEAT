package com.unleqitq.jeat.config;

import com.unleqitq.jeat.config.loader.xml.JeatXmlConfigLoader;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Collection;
import java.util.function.ToDoubleFunction;

@Builder
public class JeatConfig {
	
	/**
	 * The configuration for the mutation process.
	 */
	@NotNull
	public MutationConfig mutation;
	
	/**
	 * The configuration for the initial structure of the genome.
	 */
	@NotNull
	public InitialStructureConfig initialStructure;
	
	/**
	 * The configuration for the crossover process.
	 */
	@NotNull
	public CrossoverConfig crossover;
	
	/**
	 * The configuration for the species.
	 */
	@NotNull
	public SpeciesConfig species;
	
	/**
	 * The configuration for the distance calculation.
	 */
	@NotNull
	public DistanceConfig distance;
	
	/**
	 * The configuration for the stagnation of species.
	 */
	@NotNull
	public StagnationConfig stagnation;
	
	/**
	 * Loads the configuration from an XML file.
	 * @param file The file to load the configuration from.
	 * @param stagnationFitnessFunction The function to calculate the fitness of a species.
	 *                                  For more information, see {@link StagnationConfig#fitnessFunction}
	 * @return The configuration loaded from the file.
	 * @throws RuntimeException If an error occurs while loading the configuration.
	 */
	@NotNull
	public static JeatConfig fromXml(@NotNull File file,
		@NotNull ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction) {
		try {
			return JeatXmlConfigLoader.load(file, stagnationFitnessFunction);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Loads the configuration from XML.
	 * @param content The content of the file to load the configuration from.
	 * @param stagnationFitnessFunction The function to calculate the fitness of a species.
	 *                                  For more information, see {@link StagnationConfig#fitnessFunction}
	 * @return The configuration loaded from the file.
	 * @throws RuntimeException If an error occurs while loading the configuration.
	 */
	@NotNull
	public static JeatConfig fromXml(@NotNull String content,
		@NotNull ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction) {
		try {
			return JeatXmlConfigLoader.load(content, stagnationFitnessFunction);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
}
