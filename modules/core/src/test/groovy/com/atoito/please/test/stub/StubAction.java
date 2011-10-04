package com.atoito.please.test.stub;

import com.atoito.please.core.api.Action;

public class StubAction implements Action {

    public String toHuman() {
    	return this.getClass().getName();
    }

    public void setProperty(String key, Object value) {

    }

	public void execute() {
		// TODO Auto-generated method stub
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}
