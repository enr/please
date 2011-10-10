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

package com.atoito.please.core.operations;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.atoito.please.core.api.AbstractProvidedOperation;
import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.IdAwareOperation;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OperationResult;
import com.atoito.please.core.api.OpsFile;
import com.atoito.please.core.api.PleaseRegistry;
import com.atoito.please.core.api.RegistryFactory;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;
import com.atoito.please.core.util.Operations;
import com.atoito.please.core.util.Urls;

public class PleaseReportsOperation extends AbstractProvidedOperation implements IdAwareOperation {

    private String callId;

    public OperationResult perform() {
        M.emptyLine();
        Environment environment = Environment.getCurrent();
        PleaseRegistry registry = RegistryFactory.getRegistry();
        Map<String, Operation> operations = registry.getRegisteredOperations();
        Map<String, String> actionDefinitions = registry.getActionDefinitions();
        Set<OpsFile> opsFiles = registry.getLoadedOpsFiles();

        if ("reports".equals(callId)) {
            printPathsReport(environment);
            printOpsFilesReport(opsFiles);
            printOperationsReport(operations);
            printActionsReport(actionDefinitions);
            printClassPathReport();
        } else if ("paths".equals(callId)) {
            printPathsReport(environment);
        } else if ("operations".equals(callId)) {
            printOperationsReport(operations);
        } else if ("actions".equals(callId)) {
            printActionsReport(actionDefinitions);
        } else if ("ops".equals(callId)) {
            printOpsFilesReport(opsFiles);
        } else if ("classpath".equals(callId)) {
            printClassPathReport();
        } else {
            M.info("unknown call id %s", callId);
        }

        return Operations.successResult();
    }

    private void printOpsFilesReport(Set<OpsFile> opsFiles) {
        M.info("Loaded ops files%n----------------");
        for (OpsFile opsFile : opsFiles) {
            M.info("- %s", opsFile.toString());
        }
        M.emptyLine();
    }

    private void printPathsReport(Environment environment) {
        M.info("Paths%n-----");
        M.info("Please home : %s", environment.homeDirectory());
        M.info("User ops dir path: %s", environment.userOpsFileDirPath());
        M.info("System ops dir path: %s", environment.systemOpsFileDirPath());
        M.info("Installation ops dir path: %s", environment.distributionOpsFileDirPath());
        M.emptyLine();
    }

    private void printActionsReport(Map<String, String> actionDefinitions) {
        M.info("Actions list%n------------");
        if ((actionDefinitions == null) || (actionDefinitions.isEmpty())) {
            M.info("no action registered...");
            M.info("maybe something in the installation process has gone wrong");
        }
        TreeSet<String> keys = new TreeSet<String>(actionDefinitions.keySet());
        for (String actionId : keys) {
            String actionClass = actionDefinitions.get(actionId);
            M.info(" - %s [%s]", actionId, actionClass);
        }
        M.emptyLine();
    }

    public String toHuman() {
        return String.format("please reports operation%n-%nI give reports about the Please installation");
    }

    private void printClassPathReport() {
        M.info("Classpath%n----------");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        M.info("Classloader '%s'", cl.getClass().getName());
        M.info("Classpath entries:");
        URL[] urls = ((URLClassLoader) cl).getURLs();
        if (urls == null) {
            M.info("urls null");
        }
        try {
            for (int i = 0; i < urls.length; i++) {
                URL u = urls[i];
                String decoded = Urls.decoded(u);
                M.info(" - " + decoded);
            }
        } catch (Exception e) {
            M.info("ERROR printing classpath");
        }
        M.emptyLine();
    }

    private void printOperationsReport(Map<String, Operation> operations) {
        M.info("Operations list%n---------------");
        if ((operations == null) || (operations.isEmpty())) {
            M.info("no operation registered so far...");
            M.info("you can write down your operations in files located in ops dir");
        }
        TreeSet<String> keys = new TreeSet<String>(operations.keySet());
        for (String opid : keys) {
            Operation operation = operations.get(opid);
            if (operation instanceof DescribedOperation) {
                M.info(" - %s [ %s ]", opid, Urls.decoded(((DescribedOperation) operation).getOpsUrl()));
            } else {
                M.info(" - %s [ %s ]", opid, operation.getClass().getName());
            }
        }
        M.emptyLine();
    }

    public void setCallId(String operationId) {
        callId = operationId;
    }

}
