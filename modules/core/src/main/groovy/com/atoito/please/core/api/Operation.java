package com.atoito.please.core.api;

public interface Operation extends SelfDescribing {
	
	void validate() throws IllegalOperationStateException;
	
    OperationResult perform();

}
