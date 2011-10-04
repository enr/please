package com.atoito.please.core.api;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface PleaseRegistry {

    /*
     * the identifier for the default operation
     */
    static final String DEFAULT_OPERATION_KEY = "defaults.operation";
    
    /*
     * removes all entries in every registry
     */
    void clear();
    
    /*
     * load definitions file from plugins classpath
     */
    void loadPluginsDefinitionFiles();
    
    /*
     * load definitions file from default locations
     */
    void loadDefaultDefinitionsFiles();
    
    /*
     * load definitions from a given url
     */
    void loadDefinitionsUrl(URL definitionsUrl);
    
    /*
     * load ops file from default locations
     */
    void loadDefaultOpsFiles();

    /*
     * load a given ops file
     */
    void loadOpsFile(File maybeOpsFile);
    
    /*
     * returns a set of loaded ops files
     */
    Set<OpsFile> getLoadedOpsFiles();
    
    /*
     * returns the operation registered with key DEFAULT_OPERATION_KEY
     */
    Operation getDefaultOperation();
    
    /*
     * returns the operation registered with the given key
     */
    Operation getOperation(String operationId);
    
    boolean containsOperation(String operationId);

	Map<String, Operation> getRegisteredOperations();
	
	Map<String, String> getActionDefinitions();

    /*
     * returns the action registered with the given key
     */
    //Action getAction(String actionId);
    
    //boolean containsAction(String actionId);


}
