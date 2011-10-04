package com.atoito.please.core.exception;

@SuppressWarnings("serial")
public class PleaseException extends RuntimeException {
    /**
     * @param message
     */
    public PleaseException(String message){
      super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public PleaseException(String message, Throwable cause){
      super(message, cause);
    }

    /**
     * @param cause
     */
    public PleaseException(Throwable cause){
      super(cause);
    }
}
