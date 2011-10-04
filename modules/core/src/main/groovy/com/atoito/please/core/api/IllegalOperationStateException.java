package com.atoito.please.core.api;

@SuppressWarnings("serial")
public class IllegalOperationStateException extends Exception {

    /**
     * @param message
     */
    public IllegalOperationStateException(String message){
      super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public IllegalOperationStateException(String message, Throwable cause){
      super(message, cause);
    }

    /**
     * @param cause
     */
    public IllegalOperationStateException(Throwable cause){
      super(cause);
    }
}
