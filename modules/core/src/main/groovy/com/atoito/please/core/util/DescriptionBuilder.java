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

import java.io.File;
import java.util.List;

import com.atoito.please.core.api.Action;

public class DescriptionBuilder {

	private static final String EOL = "\n";
	private static final String ACTION_INDENT = "  ";
	private static final String OPERATION_INDENT = "";
	
	private String indent = "";
	private final StringBuffer buffer;

	public DescriptionBuilder() {
		buffer = new StringBuffer();
	}
	
	public DescriptionBuilder forAction(String actionId) {
		indent = ACTION_INDENT;
		buffer.append("* action ").append(actionId).append(EOL);
		return this;
	}
	
	public DescriptionBuilder forOperation(String operationId) {
		indent = OPERATION_INDENT;
		return append("operation ").append(operationId).eol().append("-").eol();
	}
	
	public DescriptionBuilder humanizedAs(String description) {
		return append(description).eol();
	}

	public DescriptionBuilder humanizedAs(String format, Object... args) {
		buffer.append(indent).append(String.format(format, args)).append(EOL);
		return this;
	}
	
	public DescriptionBuilder append(String text) {
		buffer.append(indent).append(text);
		return this;
	}
	
	public DescriptionBuilder append(File file) {
		buffer.append(indent).append(file.getAbsolutePath());
		return this;
	}
	
	public DescriptionBuilder append(String format, Object... args) {
		buffer.append(indent).append(String.format(format, args));
		return this;
	}
	public DescriptionBuilder appendnl(String text) {
		append(text).eol();
		return this;
	}
	
	public DescriptionBuilder appendnl(File file) {
		append(file).eol();
		return this;
	}
	
	public DescriptionBuilder appendnl(String format, Object... args) {
		append(format, args).eol();
		return this;
	}
	
	public DescriptionBuilder withUsedFile(File file, String alias, List<File> outputs) {
		return withUsedFile(file, alias, outputs, true);
	}

	public DescriptionBuilder withUsedFile(File file, String alias, List<File> outputs, boolean check) {
		buffer.append(indent);
		if (file.exists()) {
			buffer.append(alias).append(" exists");
		} else if ((outputs != null) && (outputs.contains(file))) {
			buffer.append(alias).append(" doesn't exist, but it is registered as output file from a former action");
		} else if (!check) {
			buffer.append(alias).append(" doesn't exist.").append(EOL)
			.append(indent).append("configuration says to not check; maybe this file will be created from a former action");
		} else {
			buffer.append(alias).append(" doesn't exist.").append(EOL).append(indent).append("this action fails");
		}
		return eol();
	}
	
	public DescriptionBuilder withOutputFile(File file) {
		return append("output file: ").append(file.getAbsolutePath()).eol();
	}
	
	public DescriptionBuilder withOutputFilePath(String path) {
		return append("output file: ").append(path).eol();
	}

	public DescriptionBuilder eol() {
		return append(EOL);
	}

	public String toString() {
		return buffer.toString();
	}

	public DescriptionBuilder withActions(List<Action> actions) {
		if (actions == null || actions.isEmpty()) {
			return append("no actions defined for this operation");
		}
        buffer.append(EOL).append("executed actions:").append(EOL);
    	for (Action action : actions) {
    		buffer.append(action.toHuman());
		}
    	return this;
	}
}

