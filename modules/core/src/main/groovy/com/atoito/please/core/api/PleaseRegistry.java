/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2012 - https://github.com/enr
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
    // Action getAction(String actionId);

    // boolean containsAction(String actionId);

}


