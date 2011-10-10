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
        String testDataPath = projectDirectory.getAbsolutePath() + File.separator + "src" + File.separator + "test"
                + File.separator + "data";
        System.out.printf("testDataPath '%s'%n", testDataPath);
        return new File(testDataPath);
    }

    public static File outputDir(Class<?> clazz) {
        File projectDirectory = projectDir(clazz);
        System.out.printf("project '%s'%n", projectDirectory.getAbsolutePath());
        String outputPath = projectDirectory.getAbsolutePath() + File.separator + "target" + File.separator + "test"
                + File.separator + clazz.getName();
        System.out.printf("outputPath '%s'%n", outputPath);
        return new File(outputPath);
    }
}
