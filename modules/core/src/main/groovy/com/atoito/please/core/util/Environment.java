package com.atoito.please.core.util;

import java.io.File;

import com.atoito.please.core.exception.PleaseException;
import com.google.common.base.Preconditions;

public class Environment {
	
	public static final String PLEASE_VERSION = "0.1";
	private static final Object OPS_DIR_BASENAME = "ops.d";
	private final File home;
	private final String os = System.getProperty("os.name").toLowerCase();
	
	private static Environment current;
	
	private Environment(File home) {
		this.home = home;
	}
	
	public static void initializeWithHome(File home) {
		//M.info("=========  initializeWithHome '%s'", home.getAbsolutePath());
		if (current != null) {
			throw new PleaseException("environment already initialized"); 
		}
		File cleanHome = Preconditions.checkNotNull(home, "home directory cannot be null");
		Preconditions.checkArgument(cleanHome.exists(), "home '%s' not found", cleanHome.getAbsolutePath());
		Preconditions.checkArgument(cleanHome.isDirectory(), "home '%s' is not a directory", cleanHome.getAbsolutePath());
		current = new Environment(cleanHome);
	}
	
	/**
	 * Clean environment and re initialize using home.
	 * Useful for forcing reload or in testing.
	 * 
	 * @param home the directory to use as Please home.
	 */
	public static void refreshWithHome(File home) {
		//M.info("=========  refreshWithHome '%s'", home.getAbsolutePath());
		clean();
		initializeWithHome(home);
	}
	
	public static void clean() {
		//M.info("=========  clean current = '%s'", current);
		current = null;
	}
	
	public static Environment getCurrent() {
		//M.info("=========  getCurrent current = '%s'", current);
		if (current == null) {
			throw new PleaseException("environment not yet initialized"); 
		}
		return current;
	}
	
	public File homeDirectory() {
		return home;
	}

	public String distributionOpsFileDirPath() {
        StringBuilder sb = new StringBuilder()
        .append(home.getAbsolutePath())
        .append(File.separator)
        .append("conf")
        .append(File.separator)
        .append("ops.d");
        return sb.toString();
	}
	
    public String userSettingsPath() {
        StringBuilder sb = new StringBuilder()
		.append(System.getProperty("user.home"))
		.append(File.separator)
		.append(".please")
		.append(File.separator)
		.append(Environment.PLEASE_VERSION);
        return sb.toString();
    }
    
    public String userOpsFileDirPath() {
    	return userSettingsPath() + File.separator + Environment.OPS_DIR_BASENAME;
    }

    public String systemSettingsPath() {
    	if (isWindows()) {
    		return "c:/please/"+Environment.PLEASE_VERSION;
    	}
        return "/etc/please/"+Environment.PLEASE_VERSION;
    }
    
    public String systemOpsFileDirPath() {
    	return systemSettingsPath() + File.separator + Environment.OPS_DIR_BASENAME;
    }
    
    public boolean isWindows()
    {
        return (os.indexOf("win") >= 0);
    }

    public boolean isMac()
    {
        return (os.indexOf("mac") >= 0);
    }

    public boolean isUnix()
    {
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

	public String distributionLogsDirPath() {
        StringBuilder sb = new StringBuilder()
        .append(home.getAbsolutePath())
        .append(File.separator)
        .append("logs");
        return sb.toString();
	}
}
