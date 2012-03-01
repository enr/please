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

package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.atoito.please.core.actions.TemplateAction;
import com.atoito.please.core.api.Action;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

@Test(suiteName="Actions Unit")
public class TemplateActionTest extends ActionTestBase {

    @BeforeClass
    public void setUp() throws Exception {
    	init();
    }
    
    @Test(expectedExceptions={Exception.class})
    public void testFailWithNoProperty() {
        Action action = new TemplateAction();
        action.setProperty("source", null);
        action.setProperty("destination", null);
        action.setProperty("tokens", null);
        action.initialize();
    }
    
    @Test
    public void testTemplateProcess() throws Exception {
    	String sourcePath = Joiner.on(File.separatorChar).join(testDataDir.getAbsolutePath(), "template", "tpl-01-src.txt");
        File sourceFile = new File(sourcePath);
        assertThat(sourceFile).as("source file").exists();

    	String expectedPath = Joiner.on(File.separatorChar).join(testDataDir.getAbsolutePath(), "template", "tpl-01-expected.txt");
        File expectedFile = new File(expectedPath);
        assertThat(expectedFile).as("expected file").exists();

        File resultFile = resolveFileBuildingBaseInOutputDir(this.getClass().getName(), "tpl-01-result.txt");
        
        Action action = new TemplateAction();
        action.setProperty("source", sourceFile.getAbsolutePath());
        action.setProperty("destination", resultFile.getAbsolutePath());
        
        Map<String, String> tokens = Maps.newHashMap();
        tokens.put("sentiment", "love");
        action.setProperty("tokens", tokens);
        action.initialize();
        action.execute();

        assertThat(resultFile).as("result file").exists().hasSameContentAs(expectedFile);
    }

}


