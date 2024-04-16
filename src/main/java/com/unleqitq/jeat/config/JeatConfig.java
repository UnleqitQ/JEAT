package com.unleqitq.jeat.config;

import com.unleqitq.jeat.config.loader.xml.JeatXmlConfigLoader;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.File;

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
	 * @param parameters The parameters to load the configuration.
	 * @return The configuration loaded from the file.
	 * @throws RuntimeException If an error occurs while loading the configuration.
	 */
	@NotNull
	public static JeatConfig fromXml(@NotNull File file,
		@NotNull JeatXmlConfigLoader.LoadingParameters parameters) {
		try {
			return JeatXmlConfigLoader.load(file, parameters);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Loads the configuration from XML.
	 * @param content The content of the file to load the configuration from.
	 * @param parameters The parameters to load the configuration.
	 * @return The configuration loaded from the file.
	 * @throws RuntimeException If an error occurs while loading the configuration.
	 */
	@NotNull
	public static JeatConfig fromXml(@NotNull String content,
		@NotNull JeatXmlConfigLoader.LoadingParameters parameters) {
		try {
			return JeatXmlConfigLoader.load(content, parameters);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Saves the configuration to an XML file.
	 * @param file The file to save the configuration to.
	 * @param parameters The parameters to save the configuration.
	 * @throws RuntimeException If an error occurs while saving the configuration.
	 */
	public void toXml(@NotNull File file, @NotNull JeatXmlConfigLoader.SavingParameters parameters) {
		try {
			JeatXmlConfigLoader.save(this, file, parameters);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Saves the configuration to XML.
	 * @param parameters The parameters to save the configuration.
	 * @return The content of the configuration in XML.
	 * @throws RuntimeException If an error occurs while saving the configuration.
	 */
	@NotNull
	public String toXml(@NotNull JeatXmlConfigLoader.SavingParameters parameters) {
		try {
			return JeatXmlConfigLoader.save(this, parameters);
		}
		catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
}
