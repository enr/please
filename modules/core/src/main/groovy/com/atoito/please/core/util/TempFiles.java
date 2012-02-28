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

package com.atoito.please.core.util;

import java.io.File;

public class TempFiles {

    private static final int TEMP_DIR_ATTEMPTS = 10;

    /**
     * Creates a temporary file (deleted on exit from the VM) in the given
     * directory. If untouched, the returned file will not be listed as
     * directory content (using java.io.File listFiles()).
     * 
     * @param prefix
     * @param suffix
     * @param parentDirectory
     * @return the temporary file
     */
    public static File create(String prefix, String suffix, File parent) {
        Directories.ensureExists(parent);
        String baseName = System.currentTimeMillis() + "-";
        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File newFile = new File(parent, prefix + baseName + counter + suffix);
            if (!newFile.exists()) {
                newFile.deleteOnExit();
                // System.out.printf("OK file %s%n", newFile.getAbsolutePath());
                return newFile;
            }
        }
        throw new IllegalStateException("Failed to create temporary file within " + TEMP_DIR_ATTEMPTS
                + " attempts (tried " + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ") in directory "
                + parent.getAbsolutePath());

    }
}


