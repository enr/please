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

package com.atoito.please.cli.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.atoito.please.cli.launcher.ProcessBootstrap;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.ClasspathUtil;
import com.atoito.please.core.util.M;
import com.google.common.base.Preconditions;

/**
 * Utility class pertaining the bootstrap of the CLI application.
 * 
 */
public class Bootstrap {

    /**
     * Returns a list of jars urls to include in Please classpath.
     * 
     * @return a list of jars urls to include in Please classpath.
     */
    public static List<URL> getPlease_ClasspathUrls() {
        return getPleaseClasspathUrls(getPleaseHome());
    }

    /**
     * Returns a list of jars urls to include in Please classpath, using the
     * given file as home.
     * 
     * @param home
     * @return a list of jars urls to include in Please classpath.
     */
    public static List<URL> getPleaseClasspathUrls(File home) {
        List<URL> result = new ArrayList<URL>();
        result.addAll(jarsInSubDirectory(home, "lib"));
        result.addAll(jarsInSubDirectory(home, "plugins"));
        return result;
    }

    private static File getPleaseHome() {
        File codeSource = ClasspathUtil.getClasspathForClass(ProcessBootstrap.class);
        File pleaseHome = null;
        if (codeSource.isFile()) {
            // Loaded from a JAR - assume we're running from the distribution
            pleaseHome = codeSource.getParentFile().getParentFile();
        } else {
            // Loaded from a classes dir - assume we're running from the ide or
            // tests
            pleaseHome = null;
        }
        return pleaseHome;
    }

    private static List<URL> jarsInSubDirectory(File parent, String dirName) {
        parent = Preconditions.checkNotNull(parent, "asking jars, parent directory cannot be null");
        dirName = Preconditions.checkNotNull(dirName, "asking jars, directory cannot be null");
        Preconditions.checkArgument(parent.exists(), "parent directory '%s' not found", parent.getAbsolutePath());
        Preconditions.checkArgument(parent.isDirectory(), "parent directory '%s' not a directory",
                parent.getAbsolutePath());
        String pleaseHomePath = parent.getAbsolutePath();
        List<URL> plugins = new ArrayList<URL>();
        File subDir = new File(pleaseHomePath, dirName);

        if ((subDir == null) || (!subDir.exists()) || (!subDir.isDirectory())) {
            M.info("wtf? subdirectory not found or it's not a directory... '%s'", subDir.getAbsolutePath());
            return plugins;
        }
        for (File file : subDir.listFiles()) {
            if (!file.getName().endsWith(".jar")) {
                continue;
            }
            try {
                URL url = file.toURI().toURL();
                plugins.add(url);
            } catch (MalformedURLException e) {
                throw new PleaseException("error loading jar " + file.getAbsolutePath());
            }
        }
        return plugins;
    }
}
