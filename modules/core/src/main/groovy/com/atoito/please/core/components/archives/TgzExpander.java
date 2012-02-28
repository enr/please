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

package com.atoito.please.core.components.archives;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Untar;

public class TgzExpander extends AbstractExpander {

    public void execute() {
        Untar untarrer = new Untar();
        untarrer.setProject(new Project());
        untarrer.setSrc(source);
        Untar.UntarCompressionMethod mode = new Untar.UntarCompressionMethod();
        mode.setValue("gzip");
        untarrer.setCompression(mode);
        untarrer.setDest(destination);
        // untarrer.setOverwrite(b)
        untarrer.execute();
    }

}


