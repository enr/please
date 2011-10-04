package com.atoito.please.core.api;

public class OperationResult {

	private boolean success;
	
	private boolean failure;
	
	private String output;
	
	/*
	 * non ho ancora pensato bene a cosa dovrebbe contenere
	 * in linea di massima un eccezione (se il comando la catcha e la setta)
	 * ma in altri casi?
	 * x ora potrebbe anche contenere una stringa es 'il server risponde 500' (questa situazione non da' eccezione)
	 */
	private Object failureCause;
	
	public boolean wasSuccessful() {
		return success;
	}
	
	public void fail() {
		this.failure = true;
	}
	
	/*
	 * you can mark the result as successfull only if you have not marked it yet as failure
	 */
	public void successful() {
		if (!failure) {
			this.success = true;
		}
	}

	public Object getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(Object failureCause) {
		this.failureCause = failureCause;
	}

	public void failWithCause(Object cause) {
		this.setFailureCause(cause);
		this.fail();
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
