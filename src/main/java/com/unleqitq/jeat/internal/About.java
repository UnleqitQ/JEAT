package com.unleqitq.jeat.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class About {
	
	public static String NAME;
	public static String VERSION;
	public static String AUTHOR;
	
	private About() {
		// This class should not be instantiated
	}
	
	static {
		load();
	}
	
	private static void load() {
		String content = readAboutFile();
		if (content == null) {
			NAME = "JEAT";
			VERSION = "Unknown";
			AUTHOR = "UnleqitQ";
			return;
		}
		String[] lines = content.split("\n");
		for (String line : lines) {
			String[] parts = line.split(": ");
			if (parts.length != 2) {
				continue;
			}
			String key = parts[0];
			String value = parts[1];
			Pattern stringPattern = Pattern.compile("\"([a-zA-Z0-9_\\- ]*)\"");
			switch (key) {
				case "name":
					NAME = stringPattern.matcher(value).group(1);
					break;
				case "version":
					VERSION = stringPattern.matcher(value).group(1);
					break;
				case "author":
					AUTHOR = stringPattern.matcher(value).group(1);
					break;
			}
		}
	}
	
	private static String readAboutFile() {
		String path = "/about.yml";
		InputStream stream = About.class.getResourceAsStream(path);
		if (stream == null) {
			return null;
		}
		try {
			String content = new String(stream.readAllBytes());
			stream.close();
			return content;
		}
		catch (IOException e) {
			return null;
		}
	}
	
}
