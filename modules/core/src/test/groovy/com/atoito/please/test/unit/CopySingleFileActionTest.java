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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.atoito.please.core.actions.CopyAction;
import com.atoito.please.core.api.Action;
import com.atoito.please.core.util.Directories;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class CopySingleFileActionTest extends ActionTestBase {

    String testDataPath;
    File from;

    @BeforeClass
    public void setUp() throws Exception {
    	init();
        testDataPath = testDataDir.getAbsolutePath() + File.separator;
        //baseOutputDir = Paths.outputDir(CopySingleFileActionTest.class);
        //Directories.ensureExists(baseOutputDir);
        // outputPath = outputDir.getAbsolutePath() + File.separator;
        //Directories.clean(baseOutputDir);
        String fromPath = Joiner.on(File.separatorChar).join(testDataPath, "01", "01.txt");
        from = new File(fromPath);
    }

    @Test(description = "if 'to' points to an inexistent file, the original file is copied to this path")
    public void copySingleFile() throws Exception {
        File to = resolveFile("copySingleFile", "01.copy.txt");
        if (to.exists()) {
            to.delete();
        }
        assertThat(from).as("original file").exists();
        assertThat(to).as("file to copy").doesNotExist();
        Action action = new CopyAction();
        action.setProperty("from", from.getAbsolutePath());
        action.setProperty("to", to.getAbsolutePath());
        action.initialize();
        action.execute();
        assertThat(to).as("copied file").exists().hasSameContentAs(from);
    }

    @Test(description = "if 'to' points to an existent directory, the file is copied with the original's base name")
    public void copySingleFileToExistentDirectory() throws Exception {
        File to = resolveFile("copySingleFileToExistentDirectory", "dest");
        Directories.ensureExists(to);
        assertThat(from).as("original file").exists();
        assertThat(to).as("destination directory").exists().isDirectory();
        Action action = new CopyAction();
        action.setProperty("from", from.getAbsolutePath());
        action.setProperty("to", to.getAbsolutePath());
        action.initialize();
        action.execute();
        File expected = new File(to, from.getName());
        assertThat(expected).as("copied file").exists().hasSameContentAs(from);
    }

    @Test(description = "if 'to' points to an existent file, this file is overwritten with the original's content")
    public void overwriteFile() throws Exception {
        File to = resolveFile("overwriteFile", "01.overwriteFile.txt");
        File toDir = to.getParentFile();
        Directories.ensureExists(toDir);
        assertThat(from).as("original file").exists();
        if (!to.exists()) {
            to.createNewFile();
        }
        assertThat(to).as("destination file").exists();
        Files.write("destination file", to, Charsets.UTF_8);
        String contents = Files.readFirstLine(to, Charsets.UTF_8);
        assertThat(contents).isEqualTo("destination file");
        Action action = new CopyAction();
        action.setProperty("from", from.getAbsolutePath());
        action.setProperty("to", to.getAbsolutePath());
        action.initialize();
        action.execute();
        assertThat(to).as("copied file").exists().hasSameContentAs(from);
    }

    @Test(description = "if 'from' doesn't exist and it isn't in registered outputs and checkFrom is true, action fails", expectedExceptions = { IllegalArgumentException.class })
    public void failsIfFromDoensNotExist() throws Exception {
        Action action = new CopyAction();
        action.setProperty("from", "file-" + System.currentTimeMillis());
        action.setProperty("to", "failsIfFromDoensNotExist.out");
        action.setProperty("checkFrom", true);
        action.initialize();
        action.execute();
    }
}


