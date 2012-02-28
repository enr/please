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

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Store;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class AppendAction extends AbstractAction {
    
    private Charset charset = Charsets.UTF_8;
    
    private String eol = "\n";

    /**
     * The file to append to.
     */
    private File file;
    
    /**
     * The content to append.
     */
    private String content;

    protected void internalExecute() {
        try {
            Files.append(content, file, charset);
        } catch (IOException e) {
            throw new PleaseException("error appending content to "+file, e);
        }
    }

    private File resolveAndValidateFile() {
        Object configured = Preconditions.checkNotNull(store.get("file"));
        File file = Store.toFile(configured);
        M.info("APPEND file %s", file.getAbsolutePath());
        Preconditions.checkArgument(file.exists(), "file '" + file.getAbsolutePath() + "' not found");
        return file;
    }

    private String resolveAndValidateContent() {
        Object configured = Preconditions.checkNotNull(store.get("content"));
        String content = eol + eol + configured.toString() + eol + eol;
        M.info("APPEND content %s", content);
        return content;
    }
    
    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        file = resolveAndValidateFile();
        content = resolveAndValidateContent();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ". " + Actions.dumpStore(store);
    }

    public String toHuman() {
        return new DescriptionBuilder().forAction("append").humanizedAs("append '%s' to file '%s'", content, file).toString();
    }
}


