package com.atoito.please.core.dsl;

import java.util.Map

import com.atoito.please.core.api.Action
import com.atoito.please.core.api.DescribedOperation
import com.atoito.please.core.api.Operation
import com.atoito.please.core.exception.PleaseException
import com.atoito.please.core.operations.DefaultDescribedOperation
import com.atoito.please.core.util.Actions
import com.atoito.please.core.util.M
import com.google.common.collect.Lists
import com.google.common.collect.Maps

@Mixin(DatesAbility)
public class OperationClosureDelegate implements OpsDeclaration {

    Queue<Action> operationActions = Lists.newLinkedList();
	
	/**
	 * List of files created from any action executed in the current operation.
	 * serve per permettere a un action di superare validate() nel caso operi su un file creato da un action precedente
	 */
	List<File> outputs = Lists.newArrayList()
        
    /**
     * current actions registry
     */
    Map<String, String> actionsDefinitions = Maps.newHashMap()
    
    String operationId
	
	String operationDescription;
	
	URL opsUrl
	
	def propertyMissing(String name, value) {
		if ("description".equals(name)) {
			operationDescription = value
		}
	}

    def methodMissing(String name, args) {
        def arg = ((args != null) && (args.length > 0)) ? args[0] : null
        if ("id".equals(name)) {
			if (arg == null) {
				throw new PleaseException("operation id cannot be null");
			}
            operationId = arg
            return
        }

        if (actionsDefinitions.containsKey(name)) {
            manageAction(name, arg);
            return
        } else {
            M.info("error: invoked action '${name}' not found")
			throw new PleaseException("invoked action '${name}' not found in registry");
        }
        return [name, arg]
    }
    
    def manageAction(String actionId, Object args) {
        if (args instanceof Closure) {
            String actionClassName = actionsDefinitions.get(actionId);
            Action action = Actions.newInstance(actionClassName)
            Closure closure = args
            ActionClosureDelegate actionDelegate = new ActionClosureDelegate(action)
            closure.delegate = actionDelegate
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()
            operationActions.offer(action)
            return
        }
        println "mmmhhh, invoked ${actionId} with ${args} but i was looking for a closure"
    }
    
    public Operation getOperation() {
        DescribedOperation operation = new DefaultDescribedOperation(opsUrl, operationId, operationDescription);
		for (Action action : operationActions) {
			operation.addAction(action);
		}
        return operation
    }
}
