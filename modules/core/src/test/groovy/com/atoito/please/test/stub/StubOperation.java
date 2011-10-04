package com.atoito.please.test.stub;

import com.atoito.please.core.api.IllegalOperationStateException;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OperationResult;

public class StubOperation implements Operation {

    public OperationResult perform() {
        return null;
    }

    public String toHuman() {
    	return this.getClass().getName();
    }

	public void validate() throws IllegalOperationStateException {
	}

}
