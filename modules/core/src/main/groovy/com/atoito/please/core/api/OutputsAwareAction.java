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

