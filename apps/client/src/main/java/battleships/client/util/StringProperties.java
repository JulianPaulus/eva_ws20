package battleships.client.util;

import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.util.Properties;

public class StringProperties {

	private static final StringProperties INSTANCE = new StringProperties();

	private final Properties properties;

	private StringProperties() {
		this.properties = new Properties();
		try {
			properties.load(StringProperties.class.getResourceAsStream("/strings.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(final String path) {
		return properties.getProperty(path, Strings.EMPTY);
	}

	public static StringProperties getInstance() {
		return INSTANCE;
	}

}
