package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.JeatConfig;
import com.unleqitq.jeat.genetics.genome.Genome;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.function.ToDoubleFunction;


public class JeatXmlConfigLoader {
	
	/**
	 * Load the configuration from the XML file.
	 * @param file The file to load the configuration from.
	 * @param stagnationFitnessFunction The function to calculate the fitness of a species for stagnation.
	 * @return The loaded configuration.
	 */
	@NotNull
	public static JeatConfig load(@NotNull File file,
		@NotNull ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		XmlJeatConfig xmlConfig = (XmlJeatConfig) context.createUnmarshaller().unmarshal(file);
		return xmlConfig.to(stagnationFitnessFunction);
	}
	
	/**
	 * Save the configuration to the XML file.
	 * @param config The configuration to save.
	 * @param file The file to save the configuration to.
	 */
	public static void save(@NotNull JeatConfig config, @NotNull File file) throws JAXBException {
		XmlJeatConfig xmlConfig = XmlJeatConfig.of(config);
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(xmlConfig, file);
	}
	
	/**
	 * Load the configuration from the XML file.
	 * @param content The content of the XML file.
	 * @param stagnationFitnessFunction The function to calculate the fitness of a species for stagnation.
	 * @return The loaded configuration.
	 */
	@NotNull
	public static JeatConfig load(@NotNull String content,
		@NotNull ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		XmlJeatConfig xmlConfig =
			(XmlJeatConfig) context.createUnmarshaller().unmarshal(new StringReader(content));
		return xmlConfig.to(stagnationFitnessFunction);
	}
	
	/**
	 * Save the configuration to the XML file.
	 * @param config The configuration to save.
	 * @return The content of the XML file.
	 */
	public static String save(@NotNull JeatConfig config) throws JAXBException {
		XmlJeatConfig xmlConfig = XmlJeatConfig.of(config);
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		StringWriter writer = new StringWriter();
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(xmlConfig, writer);
		return writer.toString();
	}
	
}
