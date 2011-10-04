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
