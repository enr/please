package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat
import groovy.lang.Closure;

import org.testng.annotations.Test

import com.atoito.please.core.api.DescribedOperation
import com.atoito.please.core.api.Operation
import com.atoito.please.core.dsl.OperationClosureDelegate
import com.atoito.please.core.util.M

public class OperationClosureDelegateTest {
	


    @Test(description = "OperationClosureDelegate loads operation data through missingMethod")
    public void operationLoading() {
		ExpandoMetaClass.enableGlobally()
		OperationClosureDelegate operationDelegate = new OperationClosureDelegate()
		operationDelegate.id "hello"
		operationDelegate.description = "world"
		Operation operation = operationDelegate.getOperation();
		assertThat(operation).as("described operation").isNotNull().isInstanceOf(Operation.class)
		DescribedOperation describedOperation = (DescribedOperation) operation;
        assertThat(describedOperation.getId()).as("operation id").isEqualTo("hello");
        assertThat(describedOperation.getDescription()).as("operation description").isEqualTo("world");
    }
}
