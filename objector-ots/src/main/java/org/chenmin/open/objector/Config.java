package org.chenmin.open.objector;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {

	public static String get(String key) {
		CompositeConfiguration config = init();
		return config.getString(key);
	}
	
	private static CompositeConfiguration config = null;

	private static CompositeConfiguration init() {
		if(config != null)
			return config;
		CompositeConfiguration config = new CompositeConfiguration();
		// config.addConfiguration(new SystemConfiguration());
		try {
			config.addConfiguration(new PropertiesConfiguration("objector.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}

	public static void main(String[] args) {
		System.out.println(get("ALIYUN_ACCESS_KEY"));
	}
}
