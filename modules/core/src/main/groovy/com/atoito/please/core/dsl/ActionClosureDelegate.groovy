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

package com.atoito.please.core.dsl;

import com.atoito.please.core.api.Action

@Mixin(DatesAbility)
public class ActionClosureDelegate implements OpsDeclaration {

    private Action action
    
    public ActionClosureDelegate(Action action) {
        this.action = action
    }
    
    def propertyMissing(String name, value) {
        action.setProperty(name, value)
    }
    
    def methodMissing(String name, args) {
		def arg = ((args != null) && (args.length > 0)) ? args[0] : null
        return [name, arg]
    }

}

