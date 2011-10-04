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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.Commandline;

import com.atoito.please.core.api.AbstractAction;
import com.atoito.please.core.exception.PleaseException;
import com.atoito.please.core.util.Actions;
import com.atoito.please.core.util.DescriptionBuilder;
import com.atoito.please.core.util.M;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class ExecAction extends AbstractAction {


    /**
     * Create accessors for the following, to allow different handling of
     * the output.
     */
    private ExecuteStreamHandler executeStreamHandler;
    private OutputStream outputStream;
    private OutputStream errorStream;

    /** 
     * Whether or not to append stdout/stderr to existing files
     * 
     */
    private boolean append = false;

    /**
     * The file to direct standard output from the command.
     */
    private File output;

    /**
     * The file to direct standard error from the command.
     */
    private File error;
    
    /**
     * The actual arguments for the command execution.
     */
    private String[] commandLine;
    
    /**
     * The actual, non splitted, command line.
     */
    private String originalRequest;
    
    /**
     * The working directory to call the command from.
     */
    private File workingDirectory;
    
    private boolean showOutput = false;
    
    protected void internalExecute() {

		M.info("running %s from wd %s", Arrays.asList(commandLine), workingDirectory);
        // Just call the getExecuteStreamHandler() and let it handle
        //     the semantics of instantiation or retrieval.
		Execute executor = new Execute(getExecuteStreamHandler(), null);
        executor.setWorkingDirectory(workingDirectory);

		executor.setCommandline(commandLine);
		try {
			executor.execute();
		} catch (IOException e) {
			throw new PleaseException("error executing "+Arrays.asList(commandLine)+" .");
		}
		int exit = executor.getExitValue();
		M.info("executed %s , exit value = %d%n", Arrays.asList(commandLine), exit);
		if (showOutput) /* && (output == null))*/ {
			M.info("%n---%n%s%n---%n", outputStream);
		}
		Closeables.closeQuietly(outputStream);
		Closeables.closeQuietly(errorStream);
	}
	
	private String[] resolveAndValidateCommandLine() {
		Object configured = Preconditions.checkNotNull(store.get("command"));
		// if win, ensure that starts with cmd /c ?
		originalRequest = configured.toString();
		String[] commandLine = Commandline.translateCommandline(originalRequest);
		return commandLine;
	}
	
	private File resolveAndValidateWorkingDirectory() {
		Object configured = Preconditions.checkNotNull(store.get("workingDirectory"), "working directory cannot be null");
		String path = configured.toString();
		File file = new File(path);
		if (registeredOutputs.contains(file)) {
			M.info("working directory '%s'. I don't know if it exists, but it's registered as output from a former action...", file.getAbsolutePath());
		} else {
			Preconditions.checkArgument(file.exists(), "working directory '%s' not found", file.getAbsolutePath());
			Preconditions.checkArgument(file.isDirectory(), "working directory '%s' not a directory", file.getAbsolutePath());
		}
		return file;
	}
	
	private boolean resolveAndValidateShowOutput() {
		Object show = store.get("show");
		return ((show != null) && ("true".equalsIgnoreCase(show.toString().trim())));
	}

	private File resolveAndValidateStdOutputFile() {
		return resolveAndValidateOutputFile(store.get("stdOutputFile"));
	}
	
	private File resolveAndValidateErrOutputFile() {
		return resolveAndValidateOutputFile(store.get("errOutputFile"));
	}

	private File resolveAndValidateOutputFile(Object configuration) {
		if (configuration == null) {
			return null;
		}
		String path = configuration.toString();
		File file = new File(path);
		try {
			Files.touch(file);
		} catch (IOException e) {
			throw new PleaseException("error creating output file "+file.getAbsolutePath());
		}
		return file;
	}

    /**
     * find the handler and instantiate it if it does not exist yet
     * @return handler for output and error streams
     */
    protected ExecuteStreamHandler getExecuteStreamHandler() {

        if (this.executeStreamHandler == null) {
            setExecuteStreamHandler(new PumpStreamHandler(getOutputStream(),
                                                          getErrorStream()));
        }

        return this.executeStreamHandler;
    }
    
    /**
     * sets the handler
     * @param handler a handler able of processing the output and error streams from the cvs exe
     */
    public void setExecuteStreamHandler(ExecuteStreamHandler handler) {
        this.executeStreamHandler = handler;
    }
    /**
     * access the stream to which the stdout from cvs should go
     * if this stream has already been set, it will be returned
     * if the stream has not yet been set, if the attribute output
     * has been set, the output stream will go to the output file
     * otherwise the output will go to ant's logging system
     * @return output stream to which cvs' stdout should go to
     */
    protected OutputStream getOutputStream() {

        if (this.outputStream == null) {

            if (output != null) {
                try {
                    setOutputStream(new PrintStream(
                                        new BufferedOutputStream(
                                            new FileOutputStream(output
                                                                 .getPath(),
                                                                 append))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //setOutputStream(new PrintStream(new BufferedOutputStream(new ByteArrayOutputStream())));
                setOutputStream(new ByteArrayOutputStream());
            }
        }

        return this.outputStream;
    }
    /**
     * access the stream to which the stderr from cvs should go
     * if this stream has already been set, it will be returned
     * if the stream has not yet been set, if the attribute error
     * has been set, the output stream will go to the file denoted by the error attribute
     * otherwise the stderr output will go to ant's logging system
     * @return output stream to which cvs' stderr should go to
     */
    protected OutputStream getErrorStream() {

        if (this.errorStream == null) {

            if (error != null) {

                try {
                    setErrorStream(new PrintStream(
                                       new BufferedOutputStream(
                                           new FileOutputStream(error.getPath(),
                                                                append))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                setErrorStream(new PrintStream(
                                new BufferedOutputStream(new ByteArrayOutputStream())));
            }
        }

        return this.errorStream;
    }

    /**
     * sets a stream to which the output from the cvs executable should be sent
     * @param outputStream stream to which the stdout from cvs should go
     */
    protected void setOutputStream(OutputStream outputStream) {

        this.outputStream = outputStream;
    }

    /**
     * sets a stream to which the stderr from the cvs exe should go
     * @param errorStream an output stream willing to process stderr
     */
    protected void setErrorStream(OutputStream errorStream) {

        this.errorStream = errorStream;
    }

    protected void internalInitialize() {
		Preconditions.checkNotNull(store);
		commandLine = resolveAndValidateCommandLine();
		workingDirectory = resolveAndValidateWorkingDirectory();
		output = resolveAndValidateStdOutputFile();
		error = resolveAndValidateErrOutputFile();
		showOutput = resolveAndValidateShowOutput();
	}
    
    @Override
    public String toString() {
    	return this.getClass().getName()+". "+Actions.dumpStore(store);
    }
    
	public String toHuman() {
		return new DescriptionBuilder().forAction("exec")
				.humanizedAs("exec '%s' from working directory '%s'", originalRequest, workingDirectory.getAbsolutePath())
				.appendnl((output == null) ? "no file for std output" : String.format("std output will be redirected to %s", output.getAbsolutePath()))
				.appendnl((error == null) ? "no file for err output" : String.format("err output will be redirected to %s", error.getAbsolutePath()))
				.appendnl((showOutput == true) ? "output will be showed" : "output will not be showed")
				.withUsedFile(workingDirectory, "working directory", registeredOutputs)
				.toString();
	}
}

