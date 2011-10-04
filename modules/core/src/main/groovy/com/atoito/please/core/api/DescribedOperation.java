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

package com.atoito.please.core.api;

import java.net.URL;

/**
 * Interface for operations created using dsl in ops file.
 *
 */
public interface DescribedOperation extends Operation {

	/**
	 * Returns the id the operation is described with in the ops file.
	 * 
	 * @return the operation id
	 */
	String getId();
	
	/**
	 * Returns the url of the ops file the operation is described in.
	 * 
	 * @return the url of the ops file
	 */
	URL getOpsUrl();
	
	/**
	 * Adds the given action to the list of the actions to execute to perform the operation.
	 * 
	 * @param action
	 */
    void addAction(Action action);
    
    /**
     * Returns a human readable description.
     * 
     * @return the description
     */
    String getDescription();
}

