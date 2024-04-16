package com.unleqitq.jeat.config.loader.xml;

import com.unleqitq.jeat.config.SpeciesConfig;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (name = "species")
@XmlType
public class XmlSpeciesConfig {
	
	@XmlElement (name = "compatibility-threshold", required = true)
	public double compatibilityThreshold;
	
	public static XmlSpeciesConfig of(SpeciesConfig speciesConfig) {
		XmlSpeciesConfig xmlSpeciesConfig = new XmlSpeciesConfig();
		xmlSpeciesConfig.compatibilityThreshold = speciesConfig.compatibilityThreshold;
		return xmlSpeciesConfig;
	}
	
	public SpeciesConfig to() {
		return SpeciesConfig.builder().compatibilityThreshold(compatibilityThreshold).build();
	}
	
}
