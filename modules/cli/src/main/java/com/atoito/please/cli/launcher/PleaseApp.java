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

package com.atoito.please.cli.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;

import com.atoito.please.core.api.ArgsAwareOperation;
import com.atoito.please.core.api.ClassLoaderAwareRegistry;
import com.atoito.please.core.api.IdAwareOperation;
import com.atoito.please.core.api.IllegalOperationStateException;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OperationResult;
import com.atoito.please.core.api.PleaseRegistry;
import com.atoito.please.core.api.PropertiesLoader;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.impl.DefaultPropertiesLoader;
import com.atoito.please.core.impl.DefaultRegistry;
import com.atoito.please.core.util.ClasspathUtil;
import com.atoito.please.core.util.Environment;
import com.atoito.please.core.util.M;

public class PleaseApp {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final class NullOperation implements Operation {

        String request;

        public void setRequest(String request) {
            this.request = request;
        }

        public String toHuman() {
            return String.format("null operation substitute for '%s'", request);
        }

        public OperationResult perform() {
            OperationResult result = new OperationResult();
            result.failWithCause("Operation '" + request + "' not found");
            return result;
        }

        public void validate() throws IllegalOperationStateException {

        }
    }

    private PleaseApp() {
    }

    public static void main(String[] args) {

        File location = ClasspathUtil.getClasspathForClass(PleaseApp.class);
        File home = location.getParentFile().getParentFile();

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        StatusManager sm = lc.getStatusManager();
        if (sm != null) {
            sm.add(new InfoStatus("setting up default configuration for logging.", lc));
        }

        PatternLayoutEncoder consolePatternLayout = new PatternLayoutEncoder();
        consolePatternLayout.setContext(lc);
        consolePatternLayout.setPattern("%msg%n");
        consolePatternLayout.start();

        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(lc);
        ca.setName("console");
        ca.setEncoder(consolePatternLayout);
        ca.start();

        PatternLayoutEncoder filePatternLayout = new PatternLayoutEncoder();
        filePatternLayout.setContext(lc);
        filePatternLayout.setPattern("%d{HH:mm:ss} %-5level %logger{36} - %msg%n");
        filePatternLayout.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setContext(lc);
        fileAppender.setFile(home + File.separator + "logs" + File.separator + "please.log");
        fileAppender.setName("file");
        fileAppender.setEncoder(filePatternLayout);
        fileAppender.start();

        Logger reporter = lc.getLogger("please.reporter");
        reporter.setLevel(Level.INFO);
        reporter.addAppender(ca);
        reporter.addAppender(fileAppender);
        reporter.setAdditive(false);

        lc.getLogger("com.atoito").setLevel(Level.WARN);
        lc.getLogger("com.ning").setLevel(Level.WARN);
        lc.start();

        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(ca);
        rootLogger.addAppender(fileAppender);
        rootLogger.setLevel(Level.INFO);

        PleaseApp app = new PleaseApp();
        try {
            app.run(args);
        } catch (IllegalOperationStateException e) {
            throw new PleaseException("operation not valid", e);
        }
    }

    public void run(String[] args) throws IllegalOperationStateException {

        bootEnvironment();

        // args parsing
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        if (argsList.size() == 0) {
            M.info("Please operation performer"); // %s", Environment.PLEASE_VERSION);
            M.emptyLine();
            M.info("to perform an operation, run please <operation> ...");
            M.info("to see a list of available operations, run please reports");
            M.info("add --noop option to dry-run Please. Please's 'noop' (no-operation) mode shows you what would happen, but doesn't actually do it");
            M.emptyLine();
            return;
        }

        PropertiesLoader propertiesLoader = new DefaultPropertiesLoader();
        propertiesLoader.loadFromDefaultLocations();

        String firstArg = argsList.get(0);

        argsList.remove(0);
        String[] operationArgs = argsList.toArray(EMPTY_STRING_ARRAY);

        // noop
        boolean noop = isNoop(operationArgs);

        PleaseRegistry registry = loadRegistry();

        Operation operation = loadOperation(registry, firstArg, operationArgs);

        operation.validate();

        if (noop) {
            M.info("operation not actually performed:");
            M.info(operation.toHuman());
            return;
        }

        // if valid...
        OperationResult result = operation.perform();

        processOperationResult(firstArg, result);
    }

    private void processOperationResult(String operationId, OperationResult result) {
        if (result == null) {
            M.debug("no result from operation %s", operationId);
        } else if (result.wasSuccessful()) {
            if (result.getOutput() != null) {
                M.debug(result.getOutput());
            }
        } else {
            M.debug("operation '%s' failure:%n%s", operationId, formattedCause(result));
            if (result.getOutput() != null) {
                M.debug("%n%s", result.getOutput());
            }
        }
    }

    private void bootEnvironment() {
        File location = ClasspathUtil.getClasspathForClass(this.getClass());
        File home = location.getParentFile().getParentFile();
        M.debug("starting Please with home = '%s'", home.getAbsolutePath());
        Environment.initializeWithHome(home);
    }

    private Operation loadOperation(PleaseRegistry registry, String operationId, String[] operationArgs) {
        Operation operation = null;
        File maybeOpsFile = new File(operationId);
        if ((maybeOpsFile.exists()) && (maybeOpsFile.isFile())) {
            registry.loadOpsFile(maybeOpsFile);
            operation = registry.getDefaultOperation();
        }

        if (operation == null) {
            operation = registry.getOperation(operationId);
        }

        if (operation == null) {
            M.info("no operation or ops file found for '%s'", operationId);
            operation = new NullOperation();
            ((NullOperation) operation).setRequest(operationId);
            return operation;
        }
        if (operation instanceof ArgsAwareOperation) {
            ((ArgsAwareOperation) operation).setArgs(operationArgs);
        }
        if (operation instanceof IdAwareOperation) {
            ((IdAwareOperation) operation).setCallId(operationId);
        }

        return operation;
    }

    private PleaseRegistry loadRegistry() {
        PleaseRegistry registry = DefaultRegistry.INSTANCE;
        // serve??
        if (registry instanceof ClassLoaderAwareRegistry) {
            ((ClassLoaderAwareRegistry) registry).setClassLoader(PleaseApp.class.getClassLoader());
        }
        registry.loadDefaultDefinitionsFiles();
        registry.loadPluginsDefinitionFiles();
        registry.loadDefaultOpsFiles();
        return registry;
    }

    private static String formattedCause(OperationResult result) {
        Object cause = result.getFailureCause();
        if (cause instanceof Throwable) {
            // String message = Throwables.getStackTraceAsString((Throwable)
            // cause);
            String message = ((Throwable) cause).getMessage();
            return message;
        }
        if (cause == null) {
            return "unknown cause for failure";
        }
        return cause.toString();
    }

    private boolean isNoop(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--noop")) {
                return true;
            }
        }
        return false;
    }
}


