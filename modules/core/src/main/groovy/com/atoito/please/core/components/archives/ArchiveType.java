package com.atoito.please.core.components.archives;

public enum ArchiveType {

	ZIP("zip", ".zip"), TARGZ("targz", ".tgz");

	public final String id;
	public final String defaultExtension;

	private ArchiveType(String id, String defaultExtension) {
		this.id = id;
		this.defaultExtension = defaultExtension;
	}
}
