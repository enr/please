package com.atoito.please.uat;

import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.atoito.please.core.util.M;

/**
 * A very simple smoke test which fails if any exception is throwed.
 */
public class BasicFunctionalitySmokeUat extends BaseUat{

	
	@Test(dataProvider="basic-functions", description="base functionality is working")
	public void basicFunctionsAreWorking(String[] args) {
		M.info("basicFunctionsAreWorking: %s", Arrays.asList(args));
		runApplicationWithArgs(args);
	}
	
	/*
	 * Creates arguments for basic calling
	 */
	@DataProvider(name="basic-functions")
	public Object[][] basicFunctions() {
	  return new Object[][] { 	new Object[] { new String[] {} },
			  					new Object[] { new String[] {"no-such-operation"}},
					  			new Object[] { new String[] {"reports"}},
			  					new Object[] { new String[] {"empty-op"}} };
	}

}
