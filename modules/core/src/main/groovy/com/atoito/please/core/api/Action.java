package com.atoito.please.core.api;

public interface Action extends SelfDescribing {

    void setProperty(String key, Object value);
    
    /**
     * this method should initialize and validate all needed parameters.
     * it should be called in the Operation.validate method, to check if actions are executable.
     * it should throw an exception for any problem
     * 
     */
    void initialize();
    
    void execute();
}
