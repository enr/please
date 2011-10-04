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

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Messages from Please
 */
public class M {

    private M() {
    }
    
    final static Logger logger = (Logger) LoggerFactory.getLogger("please.reporter");

    public static void debug(String message){
        logger.debug(String.format(message));
    }
    
    public static void debug(String template, Object... args){
    	logger.debug(format(template, args));
    }

    public static void info(String message){
    	logger.info(String.format(message));
    }

    public static void info(String template, Object... args){
    	logger.info(format(template, args));
    }

    private static String format(String template, Object... args) {
        return String.format(template, args);
    }

	public static void emptyLine() {
		logger.warn("");
	}

}

