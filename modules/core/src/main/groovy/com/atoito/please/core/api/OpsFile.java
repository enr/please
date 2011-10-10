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

package com.atoito.please.core.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.atoito.please.core.dsl.OpsDslEngine;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Urls;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/*
 * represents a ops-file: file declaring operations
 */
public class OpsFile {
    
    List<Operation> operations = Lists.newArrayList();
    
    private final URL url;
    
    //private final Map<String, Action> actionsRegistry;

    public OpsFile(URL opsFileUrl, Map<String, String> actionDefinitions) {
        //M.info("actionsRegistry = %s", actionDefinitions);
        url = opsFileUrl;
        M.debug("parsing %s", url.toString());
        //actionsRegistry = actions;
        String dsl = loadDsl(url);
        if ((dsl == null) || ("".equals(dsl.trim()))) {
            M.debug("dsl empty... exit");
        	return;
        }
        OpsDslEngine engine = new OpsDslEngine(url, actionDefinitions);
        engine.parse(dsl);
        operations = engine.getOperations();
    }

    @Override
    public String toString() {
    	return Urls.decoded(url);
    }

    public List<Operation> getOperations() {
        return operations;
    }
    
    private String loadDsl(URL url) {
    	URL u = Preconditions.checkNotNull(url, "ops file cannot have null url");
        String dsl = "";
    	String protocol = u.getProtocol();
    	if ("file".equals(protocol)) {
    		File resource = fileFromUrl(u);
    		if (resource.isDirectory()) {
    	        M.debug("supposed ops file %s is actually a directory...", u);
    			return dsl;
    		}
    	}
        try {
            dsl = Resources.toString(u, Charsets.UTF_8);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return dsl;
    }
    /**
     * Creates a file object from URL, managing the win backslah mess.
     * 
     * da usare al posto di FileUtils.toFile(url); perche' a volte non mi da il
     * risultato atteso verificare !
     * 
     * @param url
     *
     */
    private File fileFromUrl(URL url)
    {
        File f = null;
        try
        {
            f = new File(url.toURI());
        } catch (URISyntaxException e)
        {
            f = new File(url.getPath());
        }
        return f;
    }
}

