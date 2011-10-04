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
