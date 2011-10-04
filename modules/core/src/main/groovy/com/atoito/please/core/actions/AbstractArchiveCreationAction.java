/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2011 - https://github.com/enr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.atoito.please.core.actions;

import java.io.File;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.components.archives.ArchiveType;
import com.atoito.please.core.components.archives.Archiver;
import com.atoito.please.core.components.archives.Archives;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

/**
 * Base class for archive creation actions.
 * Just implements getArchiveType returning the correct type your action is directed to; that's all.
 *
 */
public abstract class AbstractArchiveCreationAction extends AbstractAction {

	
	/**
	 * The file to archive.
	 */
	protected File source;
	
	/**
	 * The destination for the zip.
	 * If it is a directory, the zip will be created into.
	 */
	protected File destination;
	
	/**
	 * The archiver component.
	 */
	protected Archiver archiver;

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

	protected void internalExecute() {
		M.info("archive '%s' to '%s'", source.getAbsolutePath(), destination.getAbsolutePath());
		archiver.setSource(source);
		archiver.setDestination(destination);
        if (excludes != null) {
        	archiver.setExcludes(excludes);
        }
        if (includes != null) {
        	archiver.setIncludes(includes);
        }
		archiver.execute();
		M.info("finished archive action");
	}

	private File resolveAndValidateSource() {
		Object configured = Preconditions.checkNotNull(store.get("source"), "source cannot be null");
		File file = Store.toFile(configured);
		if (!registeredOutputs.contains(file)) {
			Preconditions.checkArgument(file.exists(), "source file '%s' not found", file.getAbsolutePath());
		}
		return file;
	}

	private File resolveAndValidateDestination() {
		Object configured = Preconditions.checkNotNull(store.get("destination"), "destination cannot be null");
		File file = Store.toFile(configured);
		return file;
	}

	protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		source = resolveAndValidateSource();
		destination = resolveAndValidateDestination();
		archiver = Archives.archiverForType(getArchiveType());
		includes = getStringIfExists("includes");
		excludes =  getStringIfExists("excludes");
		registeredOutputs.add(destination);
	}
    
    private String getStringIfExists(String key) {
		Object value = store.get(key);
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	@Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		DescriptionBuilder desc = new DescriptionBuilder().forAction(getArchiveType().id)
		.humanizedAs("archive (%s) '%s' to '%s'", getArchiveType().id, source.getAbsolutePath(), destination.getAbsolutePath())
		.appendnl("includes pattern: %s", ((includes == null) ? "all files" : includes))
		.appendnl("excludes pattern: %s", ((excludes == null) ? "no file" : excludes));
		if ((destination.exists()) && (destination.isDirectory())) {
			desc.appendnl("archive file will be created in %s and registered as output file", destination.getAbsolutePath());
		} else {
			desc.appendnl("%s will be registered as output file", destination.getAbsolutePath());
		}
		desc.withUsedFile(source, "file to archive", registeredOutputs);
		return desc.toString();
	}
	
	protected abstract ArchiveType getArchiveType();
}

