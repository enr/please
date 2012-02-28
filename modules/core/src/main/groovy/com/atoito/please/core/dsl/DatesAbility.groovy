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



