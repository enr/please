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

import org.testng.annotations.Test;

/**
 *
 */
public class ActionsSequenceUat extends BaseUat {

    @Test(description = "uat for operation 'sequence-operation'")
    public void directoryCreation() throws Exception {
        // setup
        File buildDir = new File("target");
        File dataDir = new File(buildDir, "sequence-operation-data");
        File archiveDir = new File(buildDir, "sequence-operation-arch");
        File expandedDir = new File(buildDir, "sequence-operation-exp");
        String archiveName = "sequence-operation-archive.tgz";

        // app call
        String[] args = new String[] { "sequence-operation" };
        runApplicationWithArgs(args);

        // actual test
        assertThat(dataDir).as("data dir").exists();
        assertDirectoryMirrorsData(dataDir);

        assertThat(archiveDir).as("archive dir").exists();
        assertThat(new File(archiveDir, archiveName)).as("archive file").exists().isFile();

        assertThat(expandedDir).as("expanded dir").exists();
        assertDirectoryMirrorsData(expandedDir);
    }

    /*
     * tests the given root dir is actually a mirror of src/test/data/01
     */
    private void assertDirectoryMirrorsData(File root) {
        assertThat(root).as("data/01").exists().isDirectory();
        File sub = new File(root, "01");
        assertThat(sub).as("data/01/01").exists().isDirectory();
        File testDataDirRoot = new File(testDataPath + File.separator + "01");
        File testDataDirSub = new File(testDataDirRoot, "01");
        File actual01 = new File(root, "a_file.txt");
        File expected01 = new File(testDataDirRoot, "a_file.txt");
        File actual02 = new File(sub, "another.txt");
        File expected02 = new File(testDataDirSub, "another.txt");
        assertThat(actual01).as("data/01/a_file.txt").exists().isFile().hasSameContentAs(expected01);
        assertThat(actual02).as("data/01/01/another.txt").exists().isFile().hasSameContentAs(expected02);
    }
}


