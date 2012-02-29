package com.atoito.please.test.unit;

import java.io.File;

import com.atoito.please.core.util.Directories;
import com.atoito.please.test.util.Paths;
import com.google.common.base.Joiner;

public class ActionTestBase {

    protected File testDataDir;
    protected File baseOutputDir;
    
    protected void init() throws Exception {
    	testDataDir = Paths.testDataDir(ActionTestBase.class);
    	baseOutputDir = Paths.outputDir(ActionTestBase.class);
    	Directories.ensureExists(testDataDir);
    	Directories.ensureExists(baseOutputDir);
    	Directories.clean(baseOutputDir);
    }

    /*
     * Gets file named fileName, in baseDir which is in baseOutputDir.
     */
    protected File resolveDestination(String baseDir, String fileName) {
        String toPath = Joiner.on(File.separatorChar).join(baseOutputDir.getAbsolutePath(), baseDir, fileName);
        File to = new File(toPath);
        return to;
    }
}
