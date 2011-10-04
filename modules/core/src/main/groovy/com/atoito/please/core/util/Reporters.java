package com.atoito.please.core.util;

/**
 * Utility class pertaining reporters.
 *
 */
public class Reporters {
	
	private static Reporter current;

	private Reporters() { }
	
	public static Reporter defaultReporter() {
		if (current != null) {
			current.info("you are asking to create a reporter, but a reporter is already active...");
		} else {
			current = new DefaultReporter();
		}
		return current;
	}
	
}
