package com.atoito.please.core.components.archives;

import java.io.File;

public abstract class AbstractExpander implements Expander {

	File source;
	
	File destination;

	public void setArchive(File source) {
		this.source = source;
	}

	public void setDestination(File destination) {
		this.destination = destination;
	}
}
