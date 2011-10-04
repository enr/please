package com.atoito.please.core.util;

import java.util.Map;

import com.atoito.please.core.api.Action;
import com.atoito.please.core.exception.PleaseException;

public class Actions {

    public static Action newInstance(String actionClassName) {
        try {
            Class<?> actionClass = Class.forName(actionClassName);
            if (Action.class.isAssignableFrom(actionClass)) {
                Action action = (Action) actionClass.newInstance();
                return action;
            }
        } catch (Exception throwable) {
            throw new PleaseException(String.format("error creating action from class '%s': %s", actionClassName, throwable.getMessage()), throwable);
        }
        throw new PleaseException(String.format("error creating action from class '%s'", actionClassName));
    }
    
    public static String dumpStore(Map<String, Object> store) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("store = { ");
    	boolean first = true;
		for(Map.Entry<String, Object> property: store.entrySet()) {
			if (!first) {
				sb.append(", ");
			} else {
				first = false;
			}
			sb.append(property.getKey());
			sb.append(":'");
			sb.append(property.getValue());
	    	sb.append("'");
		}
		sb.append(" }");
		return sb.toString();
    }
}
