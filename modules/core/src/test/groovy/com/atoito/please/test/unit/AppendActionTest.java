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
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.atoito.please.core.actions.AppendAction;
import com.atoito.please.core.api.Action;
import com.atoito.please.core.util.Directories;
import com.atoito.please.test.util.Paths;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class AppendActionTest extends ActionTestBase {

    String testDataPath;
    File from;

    @BeforeClass
    public void setUp() throws Exception {
    	init();
        testDataPath = testDataDir.getAbsolutePath() + File.separator;
        baseOutputDir = Paths.outputDir(AppendActionTest.class);
        Directories.ensureExists(baseOutputDir);
        Directories.clean(baseOutputDir);
        String fromPath = Joiner.on(File.separatorChar).join(testDataPath, "append", "01.txt");
        from = new File(fromPath);
    }

    /*
    private File resolveDestination(String baseDir, String fileName) throws Exception {
        String toDir = Joiner.on(File.separatorChar).join(baseOutputDir.getAbsolutePath(), baseDir);
        Directories.ensureExists(new File(toDir));
        Directories.clean(new File(toDir));
        String toPath = Joiner.on(File.separatorChar).join(baseOutputDir.getAbsolutePath(), baseDir, fileName);
        File to = new File(toPath);
        return to;
    }
    */

    @Test(description = "content is appended to the file")
    public void appendContent() throws Exception {
        File to = resolveDestination("append", "01.final.txt");
        if (to.exists()) {
            to.delete();
        }
        Files.copy(from, to);
        assertThat(to).as("file to copy").exists();
        String line = "appended "+System.currentTimeMillis();
        Action action = new AppendAction();
        action.setProperty("file", to.getAbsolutePath());
        action.setProperty("content", line);
        action.initialize();
        action.execute();
        List<String> lines = Files.readLines(to, Charsets.UTF_8);
        assertThat(lines).as("final file").contains(line);
        assertThat(lines.get(lines.size() -2)).as("appended line").isEqualTo(line);
        assertThat(lines.get(lines.size() -1)).as("last line").isEqualTo("");
    }

}


