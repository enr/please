/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2011 - https://github.com/enr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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
