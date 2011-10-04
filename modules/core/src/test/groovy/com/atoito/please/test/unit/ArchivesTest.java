package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.atoito.please.core.components.archives.ArchiveType;
import com.atoito.please.core.components.archives.Archiver;
import com.atoito.please.core.components.archives.Archives;
import com.atoito.please.core.components.archives.Expander;
import com.atoito.please.core.util.Directories;
import com.atoito.please.test.util.Paths;
import com.google.common.io.Files;

/*
 * tests for .archives. package
 */
public class ArchivesTest {

	private File targetDirectory;
	
	private File sourcesDirectory;
	private File archivesDirectory;
	private File expandesDirectory;
	
	@BeforeClass
	public void setUp() {
		targetDirectory = Paths.outputDir(ArchivesTest.class);
		sourcesDirectory = new File(targetDirectory, "sources");
		archivesDirectory = new File(targetDirectory, "archives");
		expandesDirectory = new File(targetDirectory, "expandes");
	}
	
	@BeforeMethod
	public void initMethod() throws Exception {
        Directories.ensureExists(sourcesDirectory);
        Directories.clean(sourcesDirectory);
        Directories.ensureExists(archivesDirectory);
        Directories.clean(archivesDirectory);
        Directories.ensureExists(expandesDirectory);
        Directories.clean(expandesDirectory);
        assertThat(sourcesDirectory).as("sourcesDirectory").exists();
        assertThat(archivesDirectory).as("archivesDirectory").exists();
        assertThat(expandesDirectory).as("expandesDirectory").exists();
	}
	
	@Test(description = "zip archiver and expander components test")
	public void zipAndUnzip() throws Exception {
		int filesQuantity = 10;
		createFilesInSourceDir(filesQuantity);
		Archiver archiver = Archives.archiverForType(ArchiveType.ZIP);
		archiver.setSource(sourcesDirectory);
		File zipped = new File(archivesDirectory, "zipAndUnzip.zip");
		archiver.setDestination(zipped);
		archiver.execute();
		assertThat(zipped).as("destination file").exists();
		Expander expander = Archives.expanderForType(ArchiveType.ZIP);
		expander.setArchive(zipped);
		File expanded = new File(expandesDirectory, "zipAndUnzip");
		expander.setDestination(expanded);
		expander.execute();

        assertThat(expanded).as("unzipped file").isDirectory().exists();
        List<File> files = Directories.list(expanded);
        assertThat(files).as("unzipped contents").hasSize(filesQuantity);
        List<String> fileNames = new ArrayList<String>();
        for (File file : files)
        {
            System.out.printf("found file %s%n", file.getAbsolutePath());
            fileNames.add(file.getName());
        }
        Object[] expectedFileNames = new Object[] {"00001", "00002", "00003", "00004", "00005", "00006", "00007", "00008", "00009", "00010" };
        assertThat(fileNames).as("unzipped files").containsExactly(expectedFileNames);
	}
	
	private void createFilesInSourceDir(int quantity) throws Exception {
		for (int i = 1; i <= quantity; i++) {
			String fileName = String.format("%05d", i);
	        File sourceFile = new File(sourcesDirectory, fileName);
			Files.touch(sourceFile);
	        System.out.printf("created source file '%s'%n", sourceFile.getAbsolutePath());
		}
	}
}
