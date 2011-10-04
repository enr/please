package com.atoito.please.core.actions;

import com.atoito.please.core.components.archives.ArchiveType;

/**
 * Expands a targz archive.
 */
public class UntargzAction extends AbstractArchiveExpansionAction {

	@Override
	protected ArchiveType getArchiveType() {
		return ArchiveType.TARGZ;
	}

}
