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

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.atoito.please.cli.launcher.ProcessBootstrap;
import com.atoito.please.core.util.ClasspathUtil;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class BaseUat {

    /**
     * The home for the please installation used for the tests.
     */
    File installedHome;

    /**
     * The path to tests data, ie files used in tests.
     */
    String testDataPath;

    @BeforeClass
    public void setUp() throws Exception {

        File cc = ClasspathUtil.getClasspathForClass(BasicFunctionalitySmokeUat.class);
        File modules = cc.getParentFile().getParentFile().getParentFile().getParentFile();
        String installPath = Joiner.on(File.separatorChar).join(modules.getAbsolutePath(), "cli", "target", "install",
                "please");
        installedHome = new File(installPath);

        testDataPath = Joiner.on(File.separatorChar).join(modules.getAbsolutePath(), "acceptance-tests", "src", "test",
                "data");

        Environment.refreshWithHome(installedHome);
        Environment environment = Environment.getCurrent();

        String installOpsDirPath = environment.distributionOpsFileDirPath();
        File installOpsDir = new File(installOpsDirPath);
        Directories.ensureExists(installOpsDir);

        String uatOpsDirectoryPath = Joiner.on(File.separatorChar).join(modules.getAbsolutePath(), "acceptance-tests",
                "src", "test", "ops");
        File uatOpsDirectory = new File(uatOpsDirectoryPath);
        File[] uatOps = uatOpsDirectory.listFiles();
        for (int i = 0; i < uatOps.length; i++) {
            copyOpsFile(uatOps[i], installOpsDir);
        }
    }

    @AfterClass
    public void tearDown() {
        M.info("uat tearDown clean()...");
        Environment.clean();
    }

    protected void runApplicationWithArgs(String[] args) {
        ProcessBootstrap bootstrap = new ProcessBootstrap();
        bootstrap.useHome(installedHome);
        bootstrap.runWithExit(false);
        bootstrap.run("com.atoito.please.cli.launcher.PleaseApp", args);
    }

    protected void copyOpsFile(File ops, File installOpsDir) {
        File dest = new File(installOpsDir, ops.getName());
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            Files.copy(ops, dest);
        } catch (Throwable e) {
            throw new RuntimeException("error copying ops file '" + ops + "' to " + dest.getAbsolutePath(), e);
        }
    }
}


