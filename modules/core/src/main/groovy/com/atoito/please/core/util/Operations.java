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
            throw new PleaseException(String.format("error creating operation from class '%s': %s", operationClassName,
                    throwable.getMessage()), throwable);
        }
        throw new PleaseException(String.format("error creating operation from class '%s'", operationClassName));
    }

    public static OperationResult successResult() {
        OperationResult result = new OperationResult();
        result.successful();
        return result;
    }
}


