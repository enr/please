package com.atoito.please.test.stub;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OpsFile;
import com.atoito.please.core.api.PleaseRegistry;
import com.beust.jcommander.internal.Maps;

public class StubRegistry implements PleaseRegistry {
    
    Map<String, Operation> operations = Maps.newHashMap();
    Map<String, String> actionsDefinitions = Maps.newHashMap();
    
    public StubRegistry() {
        actionsDefinitions.put("act-test-simple-01", StubAction.class.getName());
        operations.put("ops-test-simple-01", new StubOperation());
    }

    public void clear() {
        // TODO Auto-generated method stub

    }

    public void loadPluginsDefinitionFiles() {
        // TODO Auto-generated method stub
    }

    public void loadDefaultDefinitionsFiles() {
        // TODO Auto-generated method stub
    }

    public void loadDefaultOpsFiles() {
        // TODO Auto-generated method stub
    }

    public void loadOpsFile(File maybeOpsFile) {
        // TODO Auto-generated method stub
    }

    public Operation getDefaultOperation() {
        // TODO Auto-generated method stub
        return null;
    }

    public Operation getOperation(String operationId) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean containsOperation(String operationId) {
        return operations.containsKey(operationId);
    }

    public Map<String, String> getActionsDefinitions() {
        return actionsDefinitions;
    }

	public Map<String, Operation> getRegisteredOperations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getActionDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<OpsFile> getLoadedOpsFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadDefinitionsUrl(URL definitionsUrl) {
		// TODO Auto-generated method stub
		
	}
}
