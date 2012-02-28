/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2012 - https://github.com/enr
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
import com.atoito.please.core.components.archives.Archives;
import com.atoito.please.core.components.archives.Expander;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

/**
 * Base class for archive expansion actions. Just implements getArchiveType
 * returning the correct type your action is directed to; that's all.
 * 
 */
public abstract class AbstractArchiveExpansionAction extends AbstractAction {

    /**
     * The archive file to expand.
     */
    protected File archive;

    /**
     * The destination directory for the expanded archive.
     * 
     */
    protected File destination;

    /**
     * The expander component.
     */
    protected Expander expander;

    protected void internalExecute() {
        M.info("expand '%s' to '%s'", archive.getAbsolutePath(), destination.getAbsolutePath());
        expander.setArchive(archive);
        expander.setDestination(destination);
        expander.execute();
    }

    private File resolveAndValidateSource() {
        Object configured = Preconditions.checkNotNull(store.get("archive"), "archive cannot be null");
        File file = Store.toFile(configured);
        if (registeredOutputs.contains(file)) {
            M.debug("I'll expand '%s'. don't know if it exists, but it's registered as output from a former action...",
                    file.getAbsolutePath());
        } else {
            Preconditions.checkArgument(file.exists(), "archive file '%s' not found", file.getAbsolutePath());
            // Preconditions.checkArgument is actually archive
        }
        return file;
    }

    private File resolveAndValidateDestination() {
        Object configured = Preconditions.checkNotNull(store.get("destination"), "destination cannot be null");
        File destination = Store.toFile(configured);
        Directories.ensureExists(destination);
        return destination;
    }

    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        archive = resolveAndValidateSource();
        destination = resolveAndValidateDestination();
        registeredOutputs.add(destination);
        expander = Archives.expanderForType(getArchiveType());
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ". " + Actions.dumpStore(store);
    }

    public String toHuman() {
        return new DescriptionBuilder().forAction("un" + getArchiveType().id)
                .humanizedAs("expand '%s' to '%s'", archive.getAbsolutePath(), destination.getAbsolutePath())
                .withOutputFile(destination).withUsedFile(archive, "archive file", registeredOutputs).toString();
    }

    protected abstract ArchiveType getArchiveType();
}


