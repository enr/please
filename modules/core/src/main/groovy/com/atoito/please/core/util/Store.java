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
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

/**
 * Utility class pertaining actions' properties storage.
 * 
 */
public class Store {

    private Store() {
    }

    public static File toFile(Object configured) {
        Object value = Preconditions.checkNotNull(configured);
        return new File(simplifyPath(value.toString()));
    }

    
	public static Map<String, String> toMap(Object configured) {
        Object checked = Preconditions.checkNotNull(configured);
        Map<String, String> map = Maps.newHashMap();
        if (! (checked instanceof Map)) {
        	return map;
        }
        Map<?, ?> raw = (Map<?, ?>) checked;        
        for (Map.Entry<?, ?>entry : raw.entrySet()) {
        	M.info("%s %s %s", entry.getKey(), entry.getValue(), entry.getValue().getClass());
        	String key = entry.getKey().toString();
        	String value = (entry.getValue() == null) ? null : entry.getValue().toString();
        	map.put(key, value);
        }
        return map;
    }

    /**
     * Returns the lexically cleaned form of the path name, <i>usually</i> (but
     * not always) equivalent to the original.
     */
    private static String simplifyPath(String pathname) {
        return Files.simplifyPath(pathname);
    }

}


