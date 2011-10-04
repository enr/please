package com.atoito.please.cli.launcher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.atoito.please.cli.util.Bootstrap;
import com.atoito.please.core.util.ClasspathUtil;
import com.atoito.please.core.util.M;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class ProcessBootstrap {

	File home;

	boolean exit = true;

	/*
	 * questo metodo serve a chiamare please programmaticamente, settando una
	 * dir home che dovrebbe contenere plugins/, config/, etc... utile per
	 * acceptance tests la differenza e' che non ha system.exit
	 */
	public void useHome(File homeDirectory) {
		home = Preconditions.checkNotNull(homeDirectory,
				"home directory cannot be null");
		Preconditions.checkArgument(home.exists(), "home '%s' not found",
				home.getAbsolutePath());
		Preconditions.checkArgument(home.isDirectory(),
				"home '%s' not a directory", home.getAbsolutePath());
	}

	/*
	 * questo metodo serve a chiamare please programmaticamente, senza che
	 * questo lanci system.exit() utile per acceptance tests
	 */
	public void runWithExit(boolean exit) {
		this.exit = exit;
	}

	public void run(String mainClassName, String[] args) {
		File location = ClasspathUtil.getClasspathForClass(ProcessBootstrap.class);

		if (home == null) {
			home = location.getParentFile().getParentFile();
		}
		
		try {
			runWithFullClasspath(mainClassName, args);
			systemExit(0);
		} catch (Throwable throwable) {
			M.info("catched : %s - %s", throwable.getClass().getName(),
					throwable.getMessage());
			Throwable cause = Throwables.getRootCause(throwable);
			M.info("cause   : %s - %s", cause.getClass().getName(),
					cause.getMessage());
			M.info(Throwables.getStackTraceAsString(cause));
			systemExit(1);
		}
	}

	private void systemExit(int code) {
		if (exit) {
			System.exit(code);
		}
	}

	private void runWithFullClasspath(String mainClassName, String[] args)
			throws Exception {
		List<URL> classpath = Bootstrap.getPleaseClasspathUrls(home);
		ClassLoader parentClassloader = ClassLoader.getSystemClassLoader()
				.getParent();
		URLClassLoader runtimeClassLoader = new URLClassLoader(
				classpath.toArray(new URL[classpath.size()]), parentClassloader);
		Thread.currentThread().setContextClassLoader(runtimeClassLoader);
		Class<?> mainClass = runtimeClassLoader.loadClass(mainClassName);
		Method mainMethod = mainClass.getMethod("main", String[].class);
		mainMethod.invoke(null, new Object[] { args });
	}

}
