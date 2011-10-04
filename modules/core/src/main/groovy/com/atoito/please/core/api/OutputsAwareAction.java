package com.atoito.please.core.api;

import java.io.File;
import java.util.List;

/**
 * interfaccia che devono implementare actions che producono un qualche file
 * in questo modo i files prodotti sono registrati tra gli output dell'operation
 * e le action successive possono decidere di validare in modo + leggero
 * un file che appartiene agli output
 * esempio un operation che ha action mkdir che crea una directory e unzip che scompatta
 * uno zip in quella directory
 * unzip sbomberebbe perche al momento della validazione la directory destination non esiste
 * se la dir e' registrata tra gli output pero' unzip puo' decidere di saltare questo controllo 
 * sull'esistenza
 *
 */
public interface OutputsAwareAction {

	void processOutputs(List<File> outputs);
}
