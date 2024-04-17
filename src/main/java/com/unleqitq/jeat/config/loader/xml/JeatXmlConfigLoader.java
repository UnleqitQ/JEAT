package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.Jeat;
import com.unleqitq.jeat.activationFunction.ActivationFunction;
import com.unleqitq.jeat.aggregationFunction.AggregationFunction;
import com.unleqitq.jeat.config.JeatConfig;
import com.unleqitq.jeat.genetics.genome.Genome;
import lombok.Builder;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;


public class JeatXmlConfigLoader {
	
	/**
	 * Load the configuration from the XML file.
	 * @param file The file to load the configuration from.
	 * @param parameters The parameters to load the configuration.
	 * @return The loaded configuration.
	 */
	@NotNull
	public static JeatConfig load(@NotNull File file, @NotNull LoadingParameters parameters)
		throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		XmlJeatConfig xmlConfig = (XmlJeatConfig) context.createUnmarshaller().unmarshal(file);
		return xmlConfig.to(parameters.activationFunctions, parameters.aggregationFunctions,
			parameters.stagnationFitnessFunction);
	}
	
	/**
	 * Save the configuration to the XML file.
	 * @param config The configuration to save.
	 * @param parameters The parameters to save the configuration.
	 * @param file The file to save the configuration to.
	 */
	public static void save(@NotNull JeatConfig config, @NotNull File file,
		@NotNull SavingParameters parameters) throws JAXBException {
		XmlJeatConfig xmlConfig = XmlJeatConfig.of(config, parameters.activationFunctionNameMapper,
			parameters.aggregationFunctionNameMapper);
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(xmlConfig, file);
	}
	
	/**
	 * Load the configuration from the XML file.
	 * @param content The content of the XML file.
	 * @param parameters The parameters to load the configuration.
	 * @return The loaded configuration.
	 */
	@NotNull
	public static JeatConfig load(@NotNull String content, @NotNull LoadingParameters parameters)
		throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		XmlJeatConfig xmlConfig =
			(XmlJeatConfig) context.createUnmarshaller().unmarshal(new StringReader(content));
		return xmlConfig.to(parameters.activationFunctions, parameters.aggregationFunctions,
			parameters.stagnationFitnessFunction);
	}
	
	/**
	 * Save the configuration to the XML file.
	 * @param config The configuration to save.
	 * @param parameters The parameters to save the configuration.
	 * @return The content of the XML file.
	 */
	public static String save(@NotNull JeatConfig config, @NotNull SavingParameters parameters)
		throws JAXBException {
		XmlJeatConfig xmlConfig = XmlJeatConfig.of(config, parameters.activationFunctionNameMapper,
			parameters.aggregationFunctionNameMapper);
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		StringWriter writer = new StringWriter();
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(xmlConfig, writer);
		return writer.toString();
	}
	
	/**
	 * Generate the schema of the XML file.
	 * @param folder The folder to save the schema to.
	 */
	public static void generateSchema(File folder) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(XmlJeatConfig.class);
		context.generateSchema(new SchemaOutputResolver() {
			@Override
			public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
				File file = new File(folder, suggestedFileName);
				Jeat.LOGGER.debug("Generating file for schema ({}: {}): {}", namespaceUri,
					suggestedFileName, file.getAbsolutePath());
				StreamResult result = new StreamResult(file);
				result.setSystemId(file.toURI().toString());
				return result;
			}
		});
	}
	
	@Builder
	public static class SavingParameters {
		
		@NotNull
		private final Function<ActivationFunction, String> activationFunctionNameMapper;
		@NotNull
		private final Function<AggregationFunction, String> aggregationFunctionNameMapper;
		
	}
	
	@Builder
	public static class LoadingParameters {
		
		@NotNull
		private final ToDoubleFunction<Collection<Genome>> stagnationFitnessFunction;
		
		@Singular
		@NotNull
		private final Map<String, ActivationFunction> activationFunctions;
		
		@Singular
		@NotNull
		private final Map<String, AggregationFunction> aggregationFunctions;
		
	}
	
}
