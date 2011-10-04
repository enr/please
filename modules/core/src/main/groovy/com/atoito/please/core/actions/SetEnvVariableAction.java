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

package com.atoito.please.core.actions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tools.ant.taskdefs.Execute;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class SetEnvVariableAction extends AbstractAction {

	String varName;
	String varValue;
	
	/**
	 * if the value has to be appended to the variable former value.
	 */
	boolean append;

	protected void internalExecute() {
		Environment environment = Environment.getCurrent();
		if (environment.isWindows()) {
			setVarInWin(varName, varValue);
		} else {
			setVarInUnixLike(varName, varValue);
		}
	}
	
	private void setVarInWin(String name, String value) {
		String command = "setx";
		if (append) {
			M.info("here I should add the variable name as prefix to the value");
		}
		String v = "\""+value+"\"";
		// setx NOMEVAR "%PATH%;C:\New Folder" 
		Execute executor = new Execute();
		String[] commandline = new String[] {
				command, name, v
		};
		executor.setCommandline(commandline);
		try {
			executor.execute();
		} catch (IOException e) {
			throw new PleaseException("error executing command "+Arrays.asList(commandline), e);
		}
	}
	
	private void setVarInUnixLike(String name, String value) {
		//Files.append(from, bashrc, charset)
		String userHome = System.getProperty("user.home");
		String bashrcPath = userHome + "/.bashrc";
		M.info("appending line to .bashrc");
		File bashrc = new File(bashrcPath);
		String varDeclaration = String.format("%nexport %s=\"%s\"%n", name, value);
		M.info(varDeclaration);
		try {
			Files.append(varDeclaration, bashrc, Charsets.UTF_8);
		} catch (IOException e) {
			throw new PleaseException("error appending to .bashrc", e);
		}
	}

	private String resolveAndValidateName() {
		Object configured = Preconditions.checkNotNull(store.get("name"));
		String name = String.valueOf(configured).trim();
		Preconditions.checkArgument((name.length() > 0), "empty var name");
		return name;
	}
	private String resolveAndValidateValue() {
		Object configured = Preconditions.checkNotNull(store.get("value"));
		return String.valueOf(configured).trim();
	}

	protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		varName = resolveAndValidateName();
		varValue = resolveAndValidateValue();
	}
    
    @Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		return new DescriptionBuilder().forAction("setenv")
				.humanizedAs("set variable '%s' with value '%s'", varName, varValue).toString();
	}
}

