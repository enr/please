package com.atoito.please.core.dsl;

import groovy.lang.Category

import java.text.SimpleDateFormat

import com.atoito.please.core.util.M

@Category(OpsDeclaration)
class DatesAbility {
	
	private static final String DEFAULT_FORMAT = "yyyyMMddHHmm";

	/**
	 * Utility method providing the current date as a string formatted as "yyyyMMddHHmm".
	 * 
	 * @return a string representing the current date
	 */
	def String date() {
		return date(DEFAULT_FORMAT);
	}
	
	/**
	* Utility method providing the current date as a string formatted against the given format.
	* 
	* @return a string representing the current date
	*/
	def String date(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String nowString = formatter.format(new Date());
		return nowString;
	}
}
