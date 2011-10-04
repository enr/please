package com.atoito.please.core.components.archives;

import java.io.File;

public interface Archiver {

	void execute();
	
	void setSource(File source);
	
	void setDestination(File destination);
	
	void setIncludes(String includes);
	
	void setExcludes(String excludes);
}
