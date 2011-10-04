package com.atoito.please.core.components.archives;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

public class ZipExpander extends AbstractExpander {

	public void execute() {
    	Expand unzipper = new Expand();
    	unzipper.setProject(new Project());
    	unzipper.setDest(destination);
    	//unzipper.setOverwrite(true);
    	unzipper.setSrc(source);
    	unzipper.execute();
	}

}
