package com.atoito.please.core.components.archives;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Untar;

public class TgzExpander extends AbstractExpander {

	public void execute() {
        Untar untarrer = new Untar();
        untarrer.setProject(new Project());
        untarrer.setSrc(source);
        Untar.UntarCompressionMethod mode = new Untar.UntarCompressionMethod();
        mode.setValue("gzip");
        untarrer.setCompression(mode);
        untarrer.setDest(destination);
        //untarrer.setOverwrite(b)
        untarrer.execute();
	}

}
