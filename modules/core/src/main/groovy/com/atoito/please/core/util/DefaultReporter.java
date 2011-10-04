package com.atoito.please.core.util;

public class DefaultReporter implements Reporter {

	public void info(String message) {
		System.out.println(message);
	}

}
