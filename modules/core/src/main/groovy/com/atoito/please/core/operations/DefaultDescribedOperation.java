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

package com.atoito.please.core.operations;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.atoito.please.core.api.Action;
import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.IllegalOperationStateException;
import com.atoito.please.core.api.OperationResult;
import com.atoito.please.core.api.OutputsAwareAction;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Operations;
import com.atoito.please.core.util.Urls;
import com.google.common.collect.Lists;

public class DefaultDescribedOperation implements DescribedOperation {
    
    private final String id;
    
    private final URL url;
    
    private final String description;
    
    private List<Action> actions = Lists.newArrayList();

    public DefaultDescribedOperation(URL url, String id, String description) {
    	this.url = url;
        this.id = id;
        this.description = description;
    }
    
    public OperationResult perform() {
    	for (Action action : actions) {
			action.execute();
		}
    	return Operations.successResult();
    }

    public String toHuman() {
    	return new DescriptionBuilder().forOperation(id)
    	.humanizedAs((description == null) ? toString() : description)
    	.withActions(actions)
    	.toString();
    }

    @Override
    public String toString() {
    	String decoded = Urls.decoded(url);
        return String.format("operation '%s' described in %s", id, decoded);
    }

	public URL getOpsUrl() {
		return url;
	}

	public String getId() {
		return this.id;
	}

    public void addAction(Action action) {
        actions.add(action);
    }

	public void validate() throws IllegalOperationStateException {
		List<File> outputs = Lists.newArrayList();
		try {
			for (Action action : actions) {
				if (action instanceof OutputsAwareAction) {
					((OutputsAwareAction) action).processOutputs(outputs);
				}
				action.initialize();
			}
		} catch (Throwable throwable) {
			throw new IllegalOperationStateException("invalid operation state: error during actions initializing", throwable);
		}
	}

	public String getDescription() {
		return description;
	}

}

