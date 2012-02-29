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

package com.atoito.please.core.actions;

import java.io.File;
import java.util.Map;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

public class TemplateAction extends AbstractAction {
	
	private File source;
	
	private File destination;
	
	private Map<String, String> tokens;

    protected void internalExecute() {

    }

    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        source = resolveAndValidateSource();
        destination = resolveAndValidateDestination();
        tokens = resolveAndValidateTokens();
    }


    private Map<String, String> resolveAndValidateTokens() {
    	Object configured = Preconditions.checkNotNull(store.get("tokens"));
    	return Store.toMap(configured);
	}

	private File resolveAndValidateDestination() {
    	Object configured = Preconditions.checkNotNull(store.get("destination"));
    	return Store.toFile(configured);
	}

	private File resolveAndValidateSource() {
    	Object configured = Preconditions.checkNotNull(store.get("source"));
    	return Store.toFile(configured);
	}

	@Override
    public String toString() {
        return this.getClass().getName() + ". " + Actions.dumpStore(store);
    }

    public String toHuman() {
    	return null;
    }
}
