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

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Directories;
import com.atoito.please.core.util.Store;
import com.google.common.base.Preconditions;

public class CopyAction extends AbstractAction {

    /**
     * The file to copy from.
     */
    private File from;

    /**
     * The file to copy to.
     */
    private File to;

    /**
     * Check if 'from' file exists? Set to false if using a from file created
     * from other actions.
     */
    private boolean checkFrom = true;

    protected void internalExecute() {
        File outputDirectory = to.getParentFile();
        Directories.ensureExists(outputDirectory);

        Copy copy = new Copy();
        copy.setProject(new Project());
        copy.setOverwrite(true);
        if (from.isDirectory()) {
            copyDirectory(from, to, copy);
        } else if ((from.isFile()) && (to.exists()) && (to.isDirectory())) {
            copyFileInDirectory(from, to, copy);
        } else {
            copyFileToFile(from, to, copy);
        }
        copy.execute();

    }

    private void copyFileInDirectory(File from, File to, Copy copy) {
        Directories.ensureExists(to);
        copy.setFile(from);
        copy.setTodir(to);

    }

    private void copyFileToFile(File fromFile, File toFile, Copy copy) {
        copy.setFile(from);
        copy.setTofile(to);
    }

    private void copyDirectory(File fromDirectory, File toDirectory, Copy copy) {
        Directories.ensureExists(to);
        FileSet fileset = new FileSet();
        fileset.setDir(from);
        copy.addFileset(fileset);
        copy.setTodir(to);
    }

    private File resolveAndValidateFrom() {
        Object stored = store.get("checkFrom");
        if (stored != null) {
            checkFrom = ("false".equalsIgnoreCase(stored.toString().trim()) ? false : true);
        }
        Object configured = Preconditions.checkNotNull(store.get("from"));
        File file = Store.toFile(configured);
        if (checkFrom && (!file.exists()) && (!registeredOutputs.contains(file))) {
            Preconditions.checkArgument(file.exists(), "file to copy '%s' not found. fail", file.getAbsolutePath());
        }
        return file;
    }

    private File resolveAndValidateTo() {
        Object configured = Preconditions.checkNotNull(store.get("to"));
        File file = Store.toFile(configured);
        return file;
    }

    /*
     * check true by default.
     */
    private boolean resolveAndValidateCheck() {
        Object configured = store.get("check");
        if (configured == null) {
            return true;
        }
        return "false".equalsIgnoreCase(configured.toString().trim());
    }

    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        from = resolveAndValidateFrom();
        to = resolveAndValidateTo();
        checkFrom = resolveAndValidateCheck();
        if (fromIsDirectoryButToIsFile(from, to)) {
            throw new IllegalStateException("from is directory but to is a file");
        }
        if ((to.exists()) && (to.isDirectory())) {
            registeredOutputs.add(new File(to, from.getName()));
        } else {
            registeredOutputs.add(to);
        }
    }

    private boolean fromIsDirectoryButToIsFile(File from, File to) {
        return ((from.isDirectory()) && (to.exists()) && (to.isFile()));
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ". " + Actions.dumpStore(store);
    }

    public String toHuman() {
        DescriptionBuilder desc = new DescriptionBuilder().forAction("copy")
                .humanizedAs("copy '%s' to '%s'", from.getAbsolutePath(), to.getAbsolutePath())
                .appendnl("<from> is file?    %s", from.isFile()).appendnl("<to> exists?       %s", to.exists());
        if (to.exists()) {
            desc.appendnl("<to> is directory? %s", to.isDirectory());
        }
        if (fromIsDirectoryButToIsFile(from, to)) {
            desc.appendnl("<from> is directory but <to> is file.").appendnl("this action fails");
        }
        if ((to.exists()) && (to.isDirectory())) {
            desc.withOutputFilePath(to.getAbsolutePath() + File.separator + from.getName());
        } else {
            desc.withOutputFile(to);
        }
        desc.withUsedFile(from, "file to copy", registeredOutputs, checkFrom);
        return desc.toString();
    }
}
