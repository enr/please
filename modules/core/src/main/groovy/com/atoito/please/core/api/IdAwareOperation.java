package com.atoito.please.core.api;

/**
 * Operations that want access to the called operation-id should implement this interface.
 *
 */
public interface IdAwareOperation {

	void setCallId(String operationId);
}
