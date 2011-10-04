package com.atoito.please.test.util;

import java.io.File;

import com.atoito.please.core.util.ClasspathUtil;

public class Paths {
    
    public static File projectDir(Class<?> clazz) {
        File codeSource = ClasspathUtil.getClasspathForClass(clazz);
        return codeSource.getParentFile().getParentFile().getParentFile();
    }
    
    public static File testDataDir(Class<?> clazz) {
		File projectDirectory = projectDir(clazz);
		System.out.printf("project '%s'%n", projectDirectory.getAbsolutePath());
		String testDataPath = projectDirectory.getAbsolutePath()
							+ File.separator
							+ "src" + File.separator + "test" + File.separator + "data";
		System.out.printf("testDataPath '%s'%n", testDataPath);
		return new File(testDataPath);
    }
    
    public static File outputDir(Class<?> clazz) {
		File projectDirectory = projectDir(clazz);
		System.out.printf("project '%s'%n", projectDirectory.getAbsolutePath());
		String outputPath = projectDirectory.getAbsolutePath()
				+ File.separator
				+ "target" + File.separator + "test" + File.separator + clazz.getName();
		System.out.printf("outputPath '%s'%n", outputPath);
		return new File(outputPath);
    }
}
