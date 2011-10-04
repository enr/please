package com.atoito.please.core.util;

import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OperationResult;
import com.atoito.please.core.exception.PleaseException;

public class Operations {

    public static Operation newInstance(String operationClassName) {
        try {
            Class<?> operationClass = Class.forName(operationClassName);
            if (Operation.class.isAssignableFrom(operationClass)) {
                Operation operation = (Operation) operationClass.newInstance();
                return operation;
            }
        } catch (Exception throwable) {
            throw new PleaseException(String.format("error creating operation from class '%s': %s", operationClassName, throwable.getMessage()), throwable);
        }
        throw new PleaseException(String.format("error creating operation from class '%s'", operationClassName));
    }
    
    public static OperationResult successResult() {
    	OperationResult result = new OperationResult();
    	result.successful();
    	return result;
    }
}
