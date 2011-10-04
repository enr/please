package com.atoito.please.core.actions;

import java.io.File;
import java.io.IOException;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

public class DeleteAction extends AbstractAction {

	/**
	 * The file to delete.
	 */
	File file;

	protected void internalExecute() {
		if (file.isDirectory()) {
			deleteAsDirectory();
		} else {
			deleteAsFile();
		}
	}

	private void deleteAsFile() {
		if (!file.delete()) {
			throw new PleaseException("error deleting file "+file);
		}
	}

	private void deleteAsDirectory() {
		try {
			Directories.delete(file);
		} catch (IOException e) {
			throw new PleaseException("error deleting directory "+file, e);
		}
	}

	private File resolveAndValidateFileToDelete() {
		Object configured = Preconditions.checkNotNull(store.get("path"));
		File file = Store.toFile(configured);
		return file;
	}

	protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		file = resolveAndValidateFileToDelete();
	}
    
    @Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		DescriptionBuilder desc = new DescriptionBuilder().forAction("delete")
		.humanizedAs("delete '%s'", file.getAbsolutePath());
		if (file.isDirectory()) {
			desc.appendnl("file to delete is directory");
			String[] files = file.list();
			String m = (Directories.isEmpty(file)) ? 
						String.format("directory contains %d files/dirs", files.length) :
						"directory is empty";
			desc.appendnl(m);
		}
		return desc.withUsedFile(file, "file to delete", registeredOutputs).toString();
	}
}
