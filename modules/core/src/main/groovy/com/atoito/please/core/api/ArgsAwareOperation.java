package com.atoito.please.core.api;

/**
 * Actions that want access to the call's arguments should implement this interface.
 *
 */
public interface ArgsAwareOperation {

    void setArgs(String[] operationArgs);

}
