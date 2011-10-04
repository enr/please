package com.atoito.please.core.components.archives;

import java.io.File;

public abstract class AbstractArchiver implements Archiver {

	protected File source;
	
	protected File destination;
	
	/**
	 * As in Ant task:
	 * comma- or space-separated list of patterns of files that must be included.
	 * All files are included when omitted.
	 * 
	 */
	protected String includes;
	
	/**
	 * As in Ant task:
	 * comma- or space-separated list of patterns of files that must be excluded.
	 * No files are excluded when omitted.
	 * 
	 */
	protected String excludes;

	public void setSource(File source) {
		this.source = source;
	}

	public void setDestination(File destination) {
		this.destination = destination;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}
}
