package com.atoito.please.core.api;

import java.net.URL;

/**
 * Interface for operations created using dsl in ops file.
 *
 */
public interface DescribedOperation extends Operation {

	/**
	 * Returns the id the operation is described with in the ops file.
	 * 
	 * @return the operation id
	 */
	String getId();
	
	/**
	 * Returns the url of the ops file the operation is described in.
	 * 
	 * @return the url of the ops file
	 */
	URL getOpsUrl();
	
	/**
	 * Adds the given action to the list of the actions to execute to perform the operation.
	 * 
	 * @param action
	 */
    void addAction(Action action);
    
    /**
     * Returns a human readable description.
     * 
     * @return the description
     */
    String getDescription();
}
