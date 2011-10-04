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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.PleaseRegistry;
import com.atoito.please.core.api.RegistryFactory;
import com.atoito.please.core.impl.DefaultRegistry;
import com.atoito.please.core.util.Environment;
import com.atoito.please.test.util.Constants;
import com.atoito.please.test.util.Paths;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class DefaultRegistryTest {

    PleaseRegistry registry;
    
    /*
     * initialize environment using a fake home.
     * we don't really need it anyway
     */
    @BeforeClass
    public void setUp() {
    	Environment.refreshWithHome(new File("."));
    }
    
    @AfterClass
    public void tearDown() {
    	Environment.clean();
    }
    
    @BeforeMethod
    public void beforeMethod() {
        registry = DefaultRegistry.INSTANCE;
    }
    
    @AfterMethod
    public void afterMethod() {
        registry.clear();
    }

    @Test(description = "registry loads operations in META-INF/plugin-definitions.please")
    public void loadPluginsDefinitions() {
        registry.loadPluginsDefinitionFiles();
        List<String> expectedOperationsId = Lists.newArrayList("plugin-operation-01", "plugin-operation-02");
        for (String id : expectedOperationsId) {
            assertThat(registry.getOperation(id)).as("operation "+id).isInstanceOf(Operation.class);
        }
    }

    @Test(description = "registry loads operations in custom definitions file")
    public void loadDefinitionsUrl() {
        registry.loadDefinitionsUrl(Resources.getResource(Constants.PATH_DEF_FILE_SIMPLE));
        List<String> expectedOperationsId = Lists.newArrayList("operation-01", "operation-02");
        for (String id : expectedOperationsId) {
            assertThat(registry.getOperation(id)).as("operation "+id).isInstanceOf(Operation.class);
        }
    }
    
 	@Test(description = "registry loads ops file")
 	public void loadOpsFile() {
 		String testDataPath = Paths.testDataDir(OpsDslEngineTest.class).getAbsolutePath() + File.separator;
 		String opsPath = Joiner.on(File.separatorChar).join(testDataPath, "ops", "01.groovy");
 		File ops = new File(opsPath);
 		PleaseRegistry registry = RegistryFactory.getRegistry();
 		registry.clear();
 		registry.loadDefinitionsUrl(Resources.getResource(Constants.PATH_DEF_FILE_REAL));
 		registry.loadOpsFile(ops);
 		Map<String, Operation> operations = registry.getRegisteredOperations();
 		Operation operation = operations.get("ops-test-01");
 		DescribedOperation describedOperation = (DescribedOperation) operation;
        assertThat(describedOperation.getId()).as("operation id").isEqualTo("ops-test-01");
 		assertThat(describedOperation.getDescription()).as("operation description").isEqualTo("a simple description");
 		Map<String, String> actions = registry.getActionDefinitions();
        assertThat(actions).as("actions definitions").isNotNull().hasSize(1)
         .includes(entry("action-01", "com.atoito.please.test.stub.StubAction"));
 	}
}

