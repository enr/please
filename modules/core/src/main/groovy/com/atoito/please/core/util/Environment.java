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
import java.net.URL;
import java.util.Properties;

import com.atoito.please.core.exception.PleaseException;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

public class Environment {

    public static final String PLEASE_VERSION;

    private static final String PLEASE_APP_PROPERTIES_RESOURCE = "please-app.properties";
    private static final String PLEASE_VERSION_PROPERTY_KEY = "please.version";
    private static final String PLEASE_VERSION_DEFAULT_VALUE = "0.1";
    
    static {
		Properties props = new Properties();
		try {
			URL url = Resources.getResource(PLEASE_APP_PROPERTIES_RESOURCE);
			props.load(url.openStream());
		} catch (Exception e) {
			M.info("error looking for please property %s", PLEASE_VERSION_PROPERTY_KEY);
		}
		String propertyValue = props.getProperty(PLEASE_VERSION_PROPERTY_KEY);
		if (propertyValue == null) {
			PLEASE_VERSION = PLEASE_VERSION_DEFAULT_VALUE;
		} else {
			PLEASE_VERSION = propertyValue;
		}
    }
    
    private static final Object OPS_DIR_BASENAME = "ops.d";
    private final File home;
    private final String os = System.getProperty("os.name").toLowerCase();

    private static Environment current;

    private Environment(File home) {
        this.home = home;
    }

    public static void initializeWithHome(File home) {
        if (current != null) {
            throw new PleaseException("environment already initialized");
        }
        File cleanHome = Preconditions.checkNotNull(home, "home directory cannot be null");
        Preconditions.checkArgument(cleanHome.exists(), "home '%s' not found", cleanHome.getAbsolutePath());
        Preconditions.checkArgument(cleanHome.isDirectory(), "home '%s' is not a directory",
                cleanHome.getAbsolutePath());
        current = new Environment(cleanHome);
    }

    /**
     * Clean environment and re initialize using home. Useful for forcing reload
     * or in testing.
     * 
     * @param home
     *            the directory to use as Please home.
     */
    public static void refreshWithHome(File home) {
        clean();
        initializeWithHome(home);
    }

    public static void clean() {
        current = null;
    }

    public static Environment getCurrent() {
        if (current == null) {
            throw new PleaseException("environment not yet initialized");
        }
        return current;
    }

    public File homeDirectory() {
        return home;
    }

    public String distributionOpsFileDirPath() {
        StringBuilder sb = new StringBuilder().append(home.getAbsolutePath()).append(File.separator).append("conf")
                .append(File.separator).append("ops.d");
        return sb.toString();
    }

    public String userSettingsPath() {
        StringBuilder sb = new StringBuilder().append(System.getProperty("user.home")).append(File.separator)
                .append(".please").append(File.separator).append(Environment.PLEASE_VERSION);
        return sb.toString();
    }

    public String userOpsFileDirPath() {
        return userSettingsPath() + File.separator + Environment.OPS_DIR_BASENAME;
    }

    public String systemSettingsPath() {
        if (isWindows()) {
            return "c:/please/" + Environment.PLEASE_VERSION;
        }
        return "/etc/please/" + Environment.PLEASE_VERSION;
    }

    public String systemOpsFileDirPath() {
        return systemSettingsPath() + "/" + Environment.OPS_DIR_BASENAME;
    }

    public boolean isWindows() {
        return (os.indexOf("win") >= 0);
    }

    public boolean isMac() {
        return (os.indexOf("mac") >= 0);
    }

    public boolean isUnix() {
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public String distributionLogsDirPath() {
        StringBuilder sb = new StringBuilder().append(home.getAbsolutePath()).append(File.separator).append("logs");
        return sb.toString();
    }

}


