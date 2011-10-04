package com.atoito.please.core.actions;

import com.atoito.please.core.components.archives.ArchiveType;

/**
 * Creates a targz archive.
 */
public class TargzAction extends AbstractArchiveCreationAction {

	@Override
	protected ArchiveType getArchiveType() {
		return ArchiveType.TARGZ;
	}

}
