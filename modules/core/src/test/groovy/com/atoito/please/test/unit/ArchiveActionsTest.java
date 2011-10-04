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

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.atoito.please.core.actions.UnzipAction;
import com.atoito.please.core.actions.ZipAction;
import com.atoito.please.core.api.Action;
import com.atoito.please.core.util.Directories;
import com.atoito.please.test.util.Paths;

public class ArchiveActionsTest {

	private String testDataPath;
	private String outputPath;
	private String zipArchivePath;
	private File expandedDirectory;

	@BeforeClass
	public void setUp() throws Exception {
		testDataPath = Paths.testDataDir(ArchiveActionsTest.class)
				.getAbsolutePath() + File.separator;
		File outputDir = Paths.outputDir(ArchiveActionsTest.class);
		Directories.ensureExists(outputDir);
		outputPath = outputDir.getAbsolutePath() + File.separator;
		zipArchivePath = outputPath + "archives-01.zip";
		Directories.clean(outputDir);
		String unzippedPath = outputPath + "archives-01-unzipped";
		expandedDirectory = new File(unzippedPath);
		Directories.ensureExists(expandedDirectory);
		Directories.clean(expandedDirectory);
	}

	@Test(description = "the given directory is zipped to the destination file")
	public void zipDirectory() throws Exception {
		String sourcePath = testDataPath + "archives-01";
		File source = new File(sourcePath);
		File destination = new File(zipArchivePath);
		assertThat(source).as("source directory").exists().isDirectory();
		assertThat(destination).as("archive").doesNotExist();
		Action action = new ZipAction();
		action.setProperty("source", sourcePath);
		action.setProperty("destination", zipArchivePath);
		action.initialize();
		action.execute();
		assertThat(destination).as("zip file").exists().isFile();
	}

	@Test(dependsOnMethods = {"zipDirectory"}, description = "the given archive is unzipped to destination")
	public void unzipArchive() {
		File source = new File(zipArchivePath);
		assertThat(source).as("zip archive").exists().isFile();
		//assertThat(destination).as("destination dir").doesNotExist();
		Action action = new UnzipAction();
		action.setProperty("archive", zipArchivePath);
		action.setProperty("destination", expandedDirectory.getAbsolutePath());
		action.initialize();
		action.execute();
		assertThat(expandedDirectory).as("destination dir").exists().isDirectory();
		File f1 = new File(expandedDirectory, "archive-01.txt");
		File f2 = new File(expandedDirectory, "sub");
		assertThat(expandedDirectory.listFiles()).as("destination directory").containsOnly(f1, f2);
		assertThat(f2).as("sub directory").isDirectory();
		assertThat(new File(f2, "archive-test-01.txt")).as("sub dir content").exists().isFile();
	}
	
}

