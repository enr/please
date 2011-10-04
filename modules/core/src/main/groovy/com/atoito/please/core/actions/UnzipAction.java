package com.atoito.please.core.actions;

import com.atoito.please.core.components.archives.ArchiveType;

/**
 * Expands a zip archive.
 */
public class UnzipAction extends AbstractArchiveExpansionAction {

	@Override
	protected ArchiveType getArchiveType() {
		return ArchiveType.ZIP;
	}

}
