package com.atoito.please.core.actions;

import java.io.File;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

public class MkdirAction extends AbstractAction {

	/**
	 * The directory to create.
	 */
	File directory;

	protected void internalExecute() {
		M.info("creating directory '%s'", directory.getAbsolutePath());
		Directories.ensureExists(directory);
	}

	private File resolveAndValidateDirectory() {
		Object configured = Preconditions.checkNotNull(store.get("directory"));
		File file = Store.toFile(configured);
		if (file.exists()) {
			Preconditions.checkArgument(file.isDirectory(), "file '"+file.getAbsolutePath()+"' exists but it's not a directory");
		}
		return file;
	}

	protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		directory = resolveAndValidateDirectory();
		registeredOutputs.add(directory);
	}
    
    @Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		return new DescriptionBuilder().forAction("mkdir")
				.humanizedAs("create directory '%s'", directory)
				.withOutputFile(directory).toString();
	}
}
