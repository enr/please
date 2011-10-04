package com.atoito.please.core.components.archives;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Tar;

public class TgzArchiver extends AbstractArchiver {

	public void execute() {
        Tar tarrer = new Tar();
        tarrer.setProject(new Project());
        tarrer.setBasedir(source);
        if (excludes != null) {
        	tarrer.setExcludes(excludes);
        }
        if (includes != null) {
        	tarrer.setIncludes(includes);
        }
        tarrer.setDestFile(destination);
        Tar.TarCompressionMethod mode = new Tar.TarCompressionMethod();
        mode.setValue("gzip");
        tarrer.setCompression(mode);
        tarrer.execute();
	}

}
