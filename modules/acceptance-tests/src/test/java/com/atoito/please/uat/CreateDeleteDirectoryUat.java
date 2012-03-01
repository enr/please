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

import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.M;

@Test(suiteName="User Acceptance")
public class CreateDeleteDirectoryUat extends BaseUat {

    File directoryUnderTest;

    @BeforeClass
    public void init() throws Exception {
        File buildDir = getBuildDir();
        String basePath = "dir-01";
        directoryUnderTest = new File(buildDir, basePath);
        if (directoryUnderTest.exists()) {
            Directories.delete(directoryUnderTest);
        }
        M.info("directoryUnderTest=%s", directoryUnderTest.getAbsolutePath());
        assertThat(directoryUnderTest).as("directory").doesNotExist();
    }

    @Test(description = "uat for operation 'create-dir'")
    public void createDirectory() {
        String[] args = new String[] { "create-dir" };
        runApplicationWithArgs(args);

        assertThat(directoryUnderTest).as("base dir").isDirectory().exists();
        assertThat(new File(directoryUnderTest, "001")).as("001").isDirectory().exists();
        assertThat(new File(directoryUnderTest, "002")).as("002").isDirectory().exists();
    }

    @Test(dependsOnMethods = { "createDirectory" }, description = "uat for operation 'delete-dir'")
    public void deleteDirectory() {
        String[] args = new String[] { "delete-dir" };
        runApplicationWithArgs(args);

        assertThat(directoryUnderTest).as("base dir").doesNotExist();
    }
}


