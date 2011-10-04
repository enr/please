package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

import java.net.URL;
import java.util.Map;

import org.testng.annotations.Test;

import com.atoito.please.core.api.DefinitionsFile;
import com.atoito.please.test.util.Constants;
import com.google.common.io.Resources;

public class DefinitionsFileTest {

    @Test(description = "DefinitionsFile contains correct mapping for actions and operations")
    public void definitionsAreCreated() {
        URL simple = Resources.getResource(Constants.PATH_DEF_FILE_SIMPLE);
        DefinitionsFile definitionsFile = new DefinitionsFile(simple);
        Map<String, String> operationDefinitions = definitionsFile.getOperationDefinitions();
        assertThat(operationDefinitions).as("operations definitions")
        .isNotNull().hasSize(2)
        .includes(entry("operation-01", "com.atoito.please.test.stub.StubOperation"))
        .includes(entry("operation-02", "com.atoito.please.test.stub.StubOperation"));
        Map<String, String> actionDefinitions = definitionsFile.getActionDefinitions();
        assertThat(actionDefinitions).as("actions definitions").isNotNull().hasSize(2)
        .includes(entry("action-01", "package.01.Action"))
        .includes(entry("action-02", "package.02.Action"));
    }
}
