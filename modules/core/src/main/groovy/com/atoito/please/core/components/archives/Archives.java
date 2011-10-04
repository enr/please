package com.atoito.please.core.components.archives;

import com.atoito.please.core.exception.PleaseException;

/**
 * Utility class pertaining Archiver and Expander components.
 *
 */
public class Archives {

	private Archives() {}
	
	public static Archiver archiverForType(ArchiveType archiveType) {
		switch (archiveType) {
		case ZIP:
			return new ZipArchiver();
		case TARGZ:
			return new TgzArchiver();
		default:
			throw new PleaseException("archive type '"+archiveType+"' not supported");
		}
	}
	
	public static Expander expanderForType(ArchiveType archiveType) {
		switch (archiveType) {
		case ZIP:
			return new ZipExpander();
		case TARGZ:
			return new TgzExpander();
		default:
			throw new PleaseException("archive type '"+archiveType+"' not supported");
		}
	}
}
