package com.atoito.please.core.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atoito.please.core.api.ClassLoaderAwareRegistry;
import com.atoito.please.core.api.DefinitionsFile;
import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OpsFile;
import com.atoito.please.core.api.PleaseRegistry;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Operations;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public enum DefaultRegistry implements PleaseRegistry, ClassLoaderAwareRegistry {

    INSTANCE;

    /*
     * actual registry for operations
     */
    Map<String, Operation> operations = Maps.newHashMap();
    
    Set<OpsFile> opsFiles = Sets.newHashSet();

    /*
     * actual registry for actions definitions
     */
    private Map<String, String> actionDefinitions = new HashMap<String, String>();

    private ClassLoader classLoader;

    private static final String PLUGIN_DEFINITIONS_PATH = "META-INF/plugin-definitions.please";
    
    private static final String DEFAULT_DEFINITIONS_FILE = "default-definitions.please";
    
    private Environment environment = Environment.getCurrent();

    public void loadDefaultOpsFiles() {

    	String pleaseDistributionOpsPath = environment.distributionOpsFileDirPath();
    	File distributionOpsDir = new File(pleaseDistributionOpsPath);
    	M.debug("loadDefaultOpsFiles %s", distributionOpsDir);
    	Directories.ensureExists(distributionOpsDir);
    	loadAllOpsInDirectory(distributionOpsDir);
    	
    	String systemOpsPath = environment.systemOpsFileDirPath();
    	File systemOpsDir = new File(systemOpsPath);
    	M.debug("loadDefaultOpsFiles %s", systemOpsDir);
    	// qua dovrebbe essere se puo' crearla la crea...
    	if ((!systemOpsDir.exists()) && (!systemOpsDir.mkdirs())) {
    		M.info("cannot create system ops dir '%s'. skipping", systemOpsPath);
    	}
//    	Directories.ensureExists(systemOpsDir);
    	loadAllOpsInDirectory(systemOpsDir);

    	String userOpsPath = environment.userOpsFileDirPath();
    	File userOpsDir = new File(userOpsPath);
    	M.debug("loadDefaultOpsFiles %s", userOpsDir);
    	Directories.ensureExists(userOpsDir);
    	loadAllOpsInDirectory(userOpsDir);
    }
    
    private void loadAllOpsInDirectory(File opsDirectory) {
    	if (opsDirectory.exists()) {
    		List<File> userOps = Directories.list(opsDirectory);
    		for (File opsFile : userOps) {
				loadOpsFile(opsFile);
			}
    	}
    }

    public void loadPluginsDefinitionFiles() {
        Enumeration<URL> pluginsDefinitions = null;
        try {
            ClassLoader cl = getClassLoader();
            pluginsDefinitions = cl.getResources(PLUGIN_DEFINITIONS_PATH);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        while (pluginsDefinitions.hasMoreElements()) {
            URL definitionsUrl = (URL) pluginsDefinitions.nextElement();
            loadDefinitionsUrl(definitionsUrl);
        }
    }
    
    public void loadDefinitionsUrl(URL definitionsUrl) {
        DefinitionsFile definitionsFile = new DefinitionsFile(definitionsUrl);
        actionDefinitions = definitionsFile.getActionDefinitions();
        Map<String, Operation> pluginOperations = operationsRegisteredIn(definitionsFile);
        operations.putAll(pluginOperations);
    }

    private ClassLoader getClassLoader() {
        if (classLoader == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return classLoader;
    }

    private Map<String, Operation> operationsRegisteredIn(DefinitionsFile definitionsFile) {
        Map<String, Operation> registeredOperations = new HashMap<String, Operation>();
        Map<String, String> pluginOperations = definitionsFile.getOperationDefinitions();
        for (Map.Entry<String, String> entry : pluginOperations.entrySet()) {
            String operationId = entry.getKey();
            String operationClassName = entry.getValue();
            Operation operation = Operations.newInstance(operationClassName);
            registeredOperations.put(operationId, operation);
        }
        return registeredOperations;
    }

    public void loadOpsFile(File opsFile) {
        try {
            loadOpsResource(opsFile.toURI().toURL());
        } catch (MalformedURLException e) {
            Throwables.propagate(e);
        }
    }

    public void loadOpsResource(URL opsFileUrl) {
        OpsFile opsFile = new OpsFile(opsFileUrl, actionDefinitions);
        opsFiles.add(opsFile);
        for(Operation operation: opsFile.getOperations()) {
        	if (operation instanceof DescribedOperation) {
        		String operationId = ((DescribedOperation) operation).getId();
           	 	this.operations.put(operationId, operation);
        	}
        }
    }

    public Operation getDefaultOperation() {
        return operations.get(DEFAULT_OPERATION_KEY);
    }

    public Operation getOperation(String operationId) {
        return operations.get(operationId);
    }

    public void clear() {
        operations.clear();
        actionDefinitions.clear();
    }

    public void loadDefaultDefinitionsFiles() {
    	ClassLoader cl = getClassLoader();
    	URL defaultDefinitionsUrl = cl.getResource(DEFAULT_DEFINITIONS_FILE);
    	loadDefinitionsUrl(defaultDefinitionsUrl);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public boolean containsOperation(String operationId) {
        return operations.containsKey(operationId);
    }

	public Map<String, Operation> getRegisteredOperations() {
		return operations;
	}

	public Map<String, String> getActionDefinitions() {
		return actionDefinitions;
	}

	public Set<OpsFile> getLoadedOpsFiles() {
		return opsFiles;
	}

}
