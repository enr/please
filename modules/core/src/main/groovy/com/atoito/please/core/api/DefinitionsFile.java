package com.atoito.please.core.api;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * It represents a definition-file:
 * file given from plugins with definitions of the exposed actions and operations.
 * 
 */
public class DefinitionsFile {
    
	/**
	 * The url of the file.
	 */
    private final URL url;
    
    /**
     * Operations defined in file.
     */
    private Map<String, String> operationDefinitions;
    
    /**
     * Actions defined in file.
     */
    private Map<String, String> actionDefinitions;
    
    public DefinitionsFile(URL definitionsUrl) {
        this.url = definitionsUrl;
        final ConfigObject conf = new ConfigSlurper().parse(url);
        operationDefinitions = definedOperations(conf);
        actionDefinitions = definedActions(conf);
    }

    private Map<String, String> definedActions(ConfigObject conf) {
        return buildDefinitions(conf.get("actions"));
    }

    private Map<String, String> definedOperations(ConfigObject conf) {
        return buildDefinitions(conf.get("operations"));
    }
    
    private Map<String, String> buildDefinitions(Object object) {
        Map<String, String> definitions = new HashMap<String, String>();
        if (object instanceof Map) {
            Map<?, ?> definitionsAsMap = (Map<?, ?>) object;
            for (Map.Entry<?, ?> entry : definitionsAsMap.entrySet()) {
                String id = entry.getKey().toString();
                String clazz = entry.getValue().toString();
                definitions.put(id, clazz);
            }
        }
        return definitions;
    }

    public Map<String, String> getOperationDefinitions() {
        return operationDefinitions;
    }

    public Map<String, String> getActionDefinitions() {
        return actionDefinitions;
    }

}
