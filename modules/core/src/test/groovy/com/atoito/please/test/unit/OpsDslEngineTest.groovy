package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat
import static org.fest.assertions.MapAssert.entry;

import java.io.File
import java.io.IOException
import java.net.URL
import java.util.Map

import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import com.atoito.please.core.api.DescribedOperation
import com.atoito.please.core.api.Operation
import com.atoito.please.core.api.PleaseRegistry
import com.atoito.please.core.api.RegistryFactory
import com.atoito.please.core.dsl.OpsDslEngine
import com.atoito.please.core.util.Environment
import com.atoito.please.test.stub.StubRegistry
import com.atoito.please.test.util.Constants
import com.atoito.please.test.util.Paths
import com.google.common.base.Charsets
import com.google.common.base.Joiner
import com.google.common.io.Resources

public class OpsDslEngineTest {

	/*
	* initialize environment using a fake home.
	* we don't really need it anyway
	*/
    @BeforeClass
    public void setUp() {
	    Environment.refreshWithHome(new File("."));
    }

    @Test(description = "smoke test for basic parsing")
    public void smokeTestWithStubRegistry() throws Exception {
        StubRegistry registry = new StubRegistry();
		URL url = Resources.getResource(Constants.PATH_OPS_FILE_SIMPLE);
        String dsl = loadDsl(url);
        OpsDslEngine engine = new OpsDslEngine(url, registry.getActionsDefinitions());
        engine.parse(dsl);
    }
    
    private String loadDsl(URL resource) throws IOException {
        String dsl = Resources.toString(resource, Charsets.UTF_8);
        return dsl;
    }
}
