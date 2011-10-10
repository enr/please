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

package com.atoito.please.core.util;

//Here is a simple utility which returns a sorted List of File objects which reside under a given root directory. It uses recursion.

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.atoito.please.core.exception.PleaseException;
import com.google.common.base.Preconditions;

/**
 * Provides utility methods for working with directories.
 * 
 * @author javapractices.com
 * @author Alex Wong
 * @author anonymous user
 */
public final class Directories {

    /**
     * Recursively walk a directory tree and return a List of all Files found;
     * the List is sorted using File.compareTo().
     * 
     * @param startingDir
     *            is a valid directory, which can be read.
     */
    static public List<File> list(File startingDir) {
        File dir = validateDirectory(startingDir);
        List<File> result = getFileListingNoSort(dir);
        Collections.sort(result);
        return result;
    }

    static private List<File> getFileListingNoSort(File aStartingDir) {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = aStartingDir.listFiles();
        for (File file : filesAndDirs) {
            result.add(file);
            if (file.isDirectory()) {
                List<File> deeperList = getFileListingNoSort(file);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    /**
     * Directory is valid if it exists, is actually a directory, and can be
     * read.
     */
    static private File validateDirectory(File directory) {
        File dir = Preconditions.checkNotNull(directory);
        Preconditions.checkArgument(dir.exists(), "Directory does not exist: %s", dir);
        Preconditions.checkArgument(dir.isDirectory(), "It's not a directory: %s", dir);
        Preconditions.checkArgument(dir.canRead(), "Directory cannot be read: %s", dir);
        return dir;
    }

    public static List<File> list(File directory, FilenameFilter filter, boolean recurse) {
        List<File> files = new ArrayList<File>();
        File[] entries = directory.listFiles();
        for (File entry : entries) {
            if (filter == null || filter.accept(directory, entry.getName())) {
                files.add(entry);
            }
            if (recurse && entry.isDirectory()) {
                files.addAll(list(entry, filter, recurse));
            }
        }
        return files;
    }

    public static List<File> list(File directory, FileFilter filter, boolean recurse) {
        List<File> files = new ArrayList<File>();
        File[] entries = directory.listFiles();
        for (File entry : entries) {
            if (filter == null || filter.accept(entry)) {
                files.add(entry);
            }
            if (recurse && entry.isDirectory()) {
                files.addAll(list(entry, filter, recurse));
            }
        }
        return files;
    }

    public static boolean isEmpty(File directory) {
        File subject = validateDirectory(directory);
        return (subject.listFiles().length < 1);
    }

    /**
     * Ensure a directory exists. If the directory not exists, it creates it. If
     * the expected directory is a normal file a RuntimeException is throwed.
     * NPE if dir is null
     * 
     * @param directory
     */
    public static void ensureExists(File directory) {
        File dir = Preconditions.checkNotNull(directory);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new RuntimeException("Expected directory '" + dir.getAbsolutePath()
                        + "' exists but it is not a directory");
            }
        } else {
            boolean created = dir.mkdirs();
            if (created == false) {
                throw new RuntimeException("Failed to create directory '" + dir.getAbsolutePath() + "'");
            }
        }
    }

    /**
     * Cleans a directory without deleting it.
     * 
     * presa da commons file utils per sostituire guava files delete (deprecata)
     * 
     * @param directory
     *            directory to clean
     * @throws IOException
     *             in case cleaning is unsuccessful
     */
    public static void clean(File directory) throws IOException {
        File cleanable = validateDirectory(directory);
        File[] files = cleanable.listFiles();
        if (files == null) { // null if security restricted
            String message = String.format("failed to list contents of '%s'.", cleanable);
            throw new PleaseException(message);
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            file.delete();
        }
    }

    /**
     * Deletes a directory recursively.
     * 
     * presa da commons file utils per sostituire guava files delete (deprecata)
     * 
     * @param directory
     *            directory to delete
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    public static void delete(File directory) throws IOException {
        File deletable = validateDirectory(directory);
        clean(deletable);
        if (!deletable.delete()) {
            String message = String.format("unable to delete directory '%s'.", deletable);
            throw new PleaseException(message);
        }
    }
}
