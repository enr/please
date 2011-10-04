package com.atoito.please.core.components.archives;

import java.io.File;

public interface Expander {

	void execute();
	
	void setArchive(File source);
	
	void setDestination(File destination);
}
