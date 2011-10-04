/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atoito.please.core.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.atoito.please.core.exception.PleaseException;
import com.google.common.base.Throwables;

/**
 * @author Hans Dockter
 */
public class ClasspathUtil {

    public static File getClasspathForClass(Class<?> targetClass) {
        URI location = null;
        try {
            location = targetClass.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            Throwables.propagate(e);
        }
        if (!location.getScheme().equals("file")) {
            throw new PleaseException(String.format("Cannot determine classpath for %s from codebase '%s'.", targetClass.getName(), location));
        }
        return new File(location.getPath());
    }


}
