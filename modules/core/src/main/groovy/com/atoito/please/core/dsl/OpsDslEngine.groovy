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

package com.atoito.please.core.dsl


import groovy.lang.Closure
import groovy.lang.ExpandoMetaClass
import groovy.lang.GroovyShell
import groovy.lang.Script

import java.util.Map

import org.codehaus.groovy.control.CompilerConfiguration

import com.atoito.please.core.api.Operation
import com.atoito.please.core.exception.PleaseException
import com.google.common.collect.Lists
import com.google.common.collect.Maps

class OpsDslEngine {
    
    List<Operation> operations = Lists.newArrayList()
    
    private Map<String, String> actionsDefinitions = Maps.newHashMap()
	
	private final URL currentOpsUrl;
    
    public OpsDslEngine(URL opsUrl, Map<String, String> actionsDefinitions) {
		this.currentOpsUrl = opsUrl
        this.actionsDefinitions = actionsDefinitions
    }

    public void parse(String dsl) {
		OpsScript dslScript = null
		try {
			dslScript = new GroovyShell(createBaseCompilerConfiguration(OpsScript.class)).parse(dsl)
		} catch (Throwable t) {
			throw new PleaseException("error parsing ops file", t)
		}
        dslScript.metaClass = createExpandoMetaClass(dslScript.class, {
            ExpandoMetaClass emc ->
              emc.actionsDefinitions = actionsDefinitions
          })
		dslScript.url(currentOpsUrl)
        dslScript.run()
        operations.addAll(dslScript.getOperations())
    }
    
    private CompilerConfiguration createBaseCompilerConfiguration(Class<? extends Script> scriptBaseClass) {
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setScriptBaseClass(scriptBaseClass.getName());
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		File targetDirectory = new File(baseDir, "com.please.target");
        configuration.setTargetDirectory(targetDirectory.getAbsolutePath())
        return configuration;
    }
    
    private ExpandoMetaClass createExpandoMetaClass(Class clazz, Closure cl){
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        cl(emc)
        emc.initialize()
        return emc
    }
}

