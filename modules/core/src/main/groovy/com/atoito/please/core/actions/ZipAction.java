package com.atoito.please.core.actions;

import com.atoito.please.core.components.archives.ArchiveType;

/**
 * Creates a zip archive.
 */
public class ZipAction extends AbstractArchiveCreationAction {

	@Override
	protected ArchiveType getArchiveType() {
		return ArchiveType.ZIP;
	}

}
