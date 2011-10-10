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
import static org.fest.assertions.MapAssert.entry;

import java.net.URL;
import java.util.Map;

import org.testng.annotations.Test;

import com.atoito.please.core.api.DefinitionsFile;
import com.atoito.please.test.util.Constants;
import com.google.common.io.Resources;

public class DefinitionsFileTest {

    @Test(description = "DefinitionsFile contains correct mapping for actions and operations")
    public void definitionsAreCreated() {
        URL simple = Resources.getResource(Constants.PATH_DEF_FILE_SIMPLE);
        DefinitionsFile definitionsFile = new DefinitionsFile(simple);
        Map<String, String> operationDefinitions = definitionsFile.getOperationDefinitions();
        assertThat(operationDefinitions).as("operations definitions").isNotNull().hasSize(2)
                .includes(entry("operation-01", "com.atoito.please.test.stub.StubOperation"))
                .includes(entry("operation-02", "com.atoito.please.test.stub.StubOperation"));
        Map<String, String> actionDefinitions = definitionsFile.getActionDefinitions();
        assertThat(actionDefinitions).as("actions definitions").isNotNull().hasSize(2)
                .includes(entry("action-01", "package.01.Action")).includes(entry("action-02", "package.02.Action"));
    }
}
