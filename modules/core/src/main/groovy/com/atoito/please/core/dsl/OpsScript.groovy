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

package com.atoito.please.core.dsl;

import groovy.lang.Closure

import java.util.List

import com.atoito.please.core.api.Operation
import com.google.common.collect.Lists


/*
 * base class for ops scripts.
 * properties added using metaclass:
 * PleaseRegistry registry
 *
 */
@Mixin(DatesAbility)
public class OpsScript extends groovy.lang.Script implements OpsDeclaration {

    List<Operation> operations = Lists.newArrayList()

    private Meta meta = new Meta();
    
    private class Meta {
        String group;
        String name;
        String version;
        String description;
		URL url;
    }

    @Override
    public Object run() {
        // cercare in sorgenti un extends Script e vedere come implementa
        return null;
    }
    
    def methodMissing(String name, args) {
        def arg = ((args != null) && (args.length > 0)) ? args[0] : null
        return [name, arg]
    }

    protected void name(String name) {
        this.meta.name = name;
    }

    protected void version(String version) {
        this.meta.version = version;
    }
    protected void group(String group) {
        this.meta.group = group;
    }
    protected void description(String description) {
        this.meta.description = description;
    }
	void url(URL url) {
        this.meta.url = url;
    }

    protected void operation(Closure closure) {
        OperationClosureDelegate operationDelegate = new OperationClosureDelegate()
        operationDelegate.actionsDefinitions = actionsDefinitions
		operationDelegate.opsUrl = meta.url
        closure.delegate = operationDelegate
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        operations.add(operationDelegate.getOperation())
    }
    
    @Override
    public String toString() {
        return "${this.getClass()} '${meta.group}:${meta.name}:${meta.version}' ${meta.url}";  // (ops:${operations}, act:${actions})";
    }

}

