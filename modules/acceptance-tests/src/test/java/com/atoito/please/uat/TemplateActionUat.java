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

package com.atoito.please.uat;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Joiner;

@Test(suiteName="User Acceptance")
public class TemplateActionUat extends BaseUat {

    @BeforeClass
    public void init() throws Exception {
    }

    @Test(description = "uat for operation 'template-operation'")
    public void processTemplate() throws Exception {
        String[] args = new String[] { "template-operation" };
        runApplicationWithArgs(args);
    	File modules = getModulesDir();
        String expectedFilePath = Joiner.on(File.separatorChar).join(modules.getAbsolutePath(), "acceptance-tests",
                "src", "test", "data", "01", "template-01-expected.txt");
        File expectedFile = new File(expectedFilePath);
        assertThat(expectedFile).as("expected file").exists();
        File resultFile = new File(getBuildDir(), "template-01-result.txt");
        assertThat(resultFile).as("result file").exists().hasSameContentAs(expectedFile);
    }
}


