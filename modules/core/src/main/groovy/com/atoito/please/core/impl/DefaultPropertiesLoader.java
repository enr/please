package com.atoito.please.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.atoito.please.core.api.PropertiesLoader;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;

public class DefaultPropertiesLoader implements PropertiesLoader {
	
	/**
	 * the string to prefix with properties you want exported with System.setProperty(key, value)
	 */
	private static final String SYSTEM_KEY_PREFIX = "system.";
	
	Environment environment = Environment.getCurrent();
	
	public void loadFromDefaultLocations() {
		loadFromUserSettingsDirectory();
		loadFromCurrentDirectory();
	}

	private void loadFromCurrentDirectory() {
		File propertiesFile = new File("please.properties");
		if (!propertiesFile.exists()) {
			return;
		}
		loadProperties(propertiesFile);
	}

	private void loadProperties(File propertiesFile) {
		try {
			Properties properties = propertiesInFile(propertiesFile);
	        for (Map.Entry<Object, Object> entry : properties.entrySet())
	        {
	            String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				M.debug("setting property '%s' = '%s'", key, value);
	            if (key.startsWith(SYSTEM_KEY_PREFIX))  {
	            	String systemKey = key.substring(SYSTEM_KEY_PREFIX.length());
	            	System.setProperty(systemKey, value);
	            }
	        }
		} catch (IOException e) {
			throw new PleaseException("error loading properties file "+propertiesFile);
		}
	}
	
	private void loadFromUserSettingsDirectory() {
		String userSettingPath = environment.userSettingsPath();
		File userSettingsDir = new File(userSettingPath);
		File propertiesFile = new File(userSettingsDir, "please.properties");
		if (!propertiesFile.exists()) {
			return;
		}
		loadProperties(propertiesFile);
	}

	/**
     * Load a properties file from the classpath
     * @param propsName
     * @return Properties
     * @throws Exception
     *
    private Properties propertiesInResource(String propsName) throws Exception {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource(propsName);
        props.load(url.openStream());
        return props;
    }
    */

    /**
     * Load a Properties File
     * @param propsFile
     * @return Properties
     * @throws IOException
     */
    private Properties propertiesInFile(File propsFile) throws IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);    
        fis.close();
        return props;
    }
}
