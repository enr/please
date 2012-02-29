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
import java.util.List;
import java.util.Map;

import com.atoito.please.core.util.M;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * classe base da estendere x creare action se estendi questa hai gia' gestito:
 * - setting dei parametri - gestione inizializzazione, in modo che la chiamata
 * ad execute() da' eccezione se prima non hai chiamato initialize()
 * 
 */
public abstract class AbstractAction implements Action, OutputsAwareAction {

    protected Map<String, Object> store = Maps.newHashMap();

    protected List<File> registeredOutputs = Lists.newArrayList();

    /**
     * If the method 'initialize' (mandatory for the correct execution) has been
     * called.
     */
    protected boolean initialized = false;

    public void setProperty(String key, Object value) {
    	M.info("key=%s value=%s", key, value);
        store.put(key, value);
    }

    public void execute() {
        Preconditions.checkArgument(initialized, "not initialized action. have you called the 'initialize()' method?");
        internalExecute();
    }

    public void initialize() {
        internalInitialize();
        initialized = true;
    }

    public void processOutputs(List<File> outputs) {
        registeredOutputs = outputs;
        internalProcessOutputs(outputs);
    }

    /**
     * An empty method to implement in subclasses to operate over outputs
     * 
     * @param outputs
     */
    protected void internalProcessOutputs(List<File> outputs) {
    }

    protected abstract void internalExecute();

    protected abstract void internalInitialize();
}


