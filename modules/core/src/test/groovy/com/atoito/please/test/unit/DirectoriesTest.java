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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.atoito.please.core.util.Directories;
import com.google.common.io.Files;

@Test(suiteName="Components and Api")
public class DirectoriesTest {

    private static FileFilter javaFileFilter = new FileFilter() {
        public boolean accept(File file) {
            return ((file.isFile()) && (file.getAbsolutePath().endsWith(".java")));
        }
    };

    private String thisFile;

    @BeforeClass
    public void initData() {
        thisFile = "." + File.separator + "src" + File.separator + "test" + File.separator + "groovy" + File.separator
                + this.getClass().getCanonicalName().replace(".", File.separator) + ".java";
    }

    @Test
    public void testFilteredListing() {
        File startingDirectory = new File(".");
        List<File> files = Directories.list(startingDirectory, javaFileFilter, true);

        List<String> filePaths = new ArrayList<String>();
        for (File file : files) {
            filePaths.add(file.getPath());
        }
        assertThat(filePaths).as("files list").contains(thisFile);
    }

    @Test
    public void testListing() {
        File startingDirectory = new File(".");
        List<File> files = Directories.list(startingDirectory);

        List<String> fileNames = new ArrayList<String>();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        assertThat(fileNames).as("files list").contains("core.gradle");

    }

    @Test
    public void testIsEmpty() {
        File pwd = new File(".");
        assertThat(Directories.isEmpty(pwd)).isFalse();
        File newDir = Files.createTempDir();
        assertThat(Directories.isEmpty(newDir)).isTrue();
    }

}


