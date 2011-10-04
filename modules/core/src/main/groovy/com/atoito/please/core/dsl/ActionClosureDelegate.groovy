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
