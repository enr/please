package com.atoito.please.core.components.archives;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

public class ZipArchiver extends AbstractArchiver {

	public void execute() {
        Zip zipper = new Zip();
        zipper.setProject(new Project());
        zipper.setBasedir(source);
        if (excludes != null) {
        	zipper.setExcludes(excludes);
        }
        if (includes != null) {
        	zipper.setIncludes(includes);
        }
        zipper.setDestFile(destination);
        zipper.execute();
	}

}
