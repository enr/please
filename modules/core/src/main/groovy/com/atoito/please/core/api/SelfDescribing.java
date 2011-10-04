package com.atoito.please.core.api;

/**
 * Operations and actions that want provide a human descriptions of themselves when called with --noop option, should implement this interface.
 */
public interface SelfDescribing {

    String toHuman();
}
