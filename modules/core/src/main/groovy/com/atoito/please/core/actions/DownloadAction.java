package com.atoito.please.core.actions;

import java.io.File;
import java.io.IOException;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.components.DefaultDownloader;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class DownloadAction extends AbstractAction {

	/**
	 * The file to save.
	 */
	private File destination;
	
	/**
	 * The url to fetch.
	 */
	private String url;
	
	protected void internalExecute() {
		DefaultDownloader d = new DefaultDownloader();
		File downloaded = d.fetch(url, destination.getAbsolutePath());
		M.info("saved '%s' to '%s'", url, downloaded.getAbsolutePath());
	}

	protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		url = resolveAndValidateUrl();
		destination = resolveAndValidateDestination();
		registeredOutputs.add(destination);
	}

	private String resolveAndValidateUrl() {
		Object configured = Preconditions.checkNotNull(store.get("url"));
		return configured.toString();

	}

	private File resolveAndValidateDestination() {
		Object configured = Preconditions.checkNotNull(store.get("destination"));
		File file = Store.toFile(configured);
		try {
			Files.touch(file);
		} catch (IOException e) {
			throw new PleaseException("error creating output file "+file.getAbsolutePath());
		}
		return file;
	}
    
    @Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		return new DescriptionBuilder().forAction("download")
				.humanizedAs("download '%s' to '%s'", url, destination.getAbsolutePath())
				.withOutputFile(destination).toString();
	}
}
