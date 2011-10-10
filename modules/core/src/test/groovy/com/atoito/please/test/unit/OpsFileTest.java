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

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OpsFile;
import com.atoito.please.test.stub.StubRegistry;
import com.atoito.please.test.util.Constants;
import com.google.common.io.Resources;

public class OpsFileTest {

    @Test(description = "ops file is loaded")
    public void testSimple() {
        StubRegistry registry = new StubRegistry();
        OpsFile opsFile = new OpsFile(Resources.getResource(Constants.PATH_OPS_FILE_SIMPLE),
                registry.getActionsDefinitions());
        List<Operation> operations = opsFile.getOperations();
        assertThat(operations).as("operations").isNotNull().isNotEmpty().hasSize(1);
        Operation operation = operations.get(0);
        assertThat(operation).as("operation").isNotNull().isInstanceOf(DescribedOperation.class);
        DescribedOperation describedOperation = (DescribedOperation) operation;
        assertThat(describedOperation.getId()).as("operation id").isEqualTo("ops-test-simple-01");
        assertThat(describedOperation.getDescription()).as("operation description").isEqualTo("a simple description");
    }
}
