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

