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

package com.atoito.please.core.actions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.stringtemplate.v4.ST;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

/**
 * Note:
 * - empty lines are trimmed.
 * - Stringtemplate fails with delimiters side by side without placeholder in the middle (ie $$)
 *
 */
public class TemplateAction extends AbstractAction {
	
	private static final char DEFAULT_START_DELIMITER_CHAR = '$';
	private static final char DEFAULT_STOP_DELIMITER_CHAR = '$';
	
	private static final String START_DELIMITER_STORAGE_KEY = "startchar";
	private static final String STOP_DELIMITER_STORAGE_KEY = "stopchar";

	private File source;
	
	private File destination;
	
	private char startDelimiter;
	
	private char stopDelimiter;
	
	private Map<String, String> tokens;
	
	private Charset encoding = Charsets.UTF_8;

    protected void internalExecute() {
    	String template = "";
    	try {
			template = Files.toString(source, encoding);
		} catch (IOException e) {
            throw new PleaseException("error reading file " + source.getAbsolutePath());
		}
    	M.info("starting ST with delimiters '%s' '%s'", startDelimiter, stopDelimiter);
    	ST st = new ST(template, startDelimiter, stopDelimiter);
    	for (Map.Entry<String, String>token : tokens.entrySet()) {
    		st.add(token.getKey(), token.getValue());
    	}
    	String result = st.render();
    	M.info(result);
    	try {
			Files.write(result, destination, encoding);
		} catch (IOException e) {
			throw new PleaseException("error writing file " + destination.getAbsolutePath());
		}
    }

    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        source = resolveAndValidateSource();
        destination = resolveAndValidateDestination();
        tokens = resolveAndValidateTokens();
        startDelimiter = resolveAndValidateStartDelimiter();
        stopDelimiter = resolveAndValidateStopDelimiter();
    }

    private char resolveAndValidateStartDelimiter() {
    	return resolveAndValidateDelimiter(START_DELIMITER_STORAGE_KEY, DEFAULT_START_DELIMITER_CHAR);
    }

    private char resolveAndValidateStopDelimiter() {
    	return resolveAndValidateDelimiter(STOP_DELIMITER_STORAGE_KEY, DEFAULT_STOP_DELIMITER_CHAR);
    }

    private char resolveAndValidateDelimiter(String storeKey, char defaultDelimiterChar) {
    	Object configured = store.get(storeKey);
    	M.info("configured %s = '%s'", storeKey, configured);
    	if (configured == null) {
    		return defaultDelimiterChar;
    	}
    	M.info("configured delimiter class = %s", configured.getClass().getName());
    	String value = configured.toString();
		Preconditions.checkArgument((value.length() == 1), "%s should be 1 char long", storeKey);
    	return value.charAt(0);
	}

	private Map<String, String> resolveAndValidateTokens() {
    	Object configured = Preconditions.checkNotNull(store.get("tokens"));
    	return Store.toMap(configured);
	}

	private File resolveAndValidateDestination() {
    	Object configured = Preconditions.checkNotNull(store.get("destination"));
        File file = Store.toFile(configured);
        try {
            Files.touch(file);
        } catch (IOException e) {
            throw new PleaseException("error creating output file " + file.getAbsolutePath());
        }
        return file;
	}

	private File resolveAndValidateSource() {
    	Object configured = Preconditions.checkNotNull(store.get("source"));
    	File sourceFile = Store.toFile(configured);
    	Preconditions.checkArgument(sourceFile.exists(), String.format("template file %s not exists", sourceFile.getAbsolutePath()));
    	return sourceFile;
	}

	@Override
    public String toString() {
        return this.getClass().getName() + ". " + Actions.dumpStore(store);
    }

    public String toHuman() {
    	return null;
    }
}
