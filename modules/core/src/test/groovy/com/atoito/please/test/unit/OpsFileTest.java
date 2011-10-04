package com.atoito.please.test.unit;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.atoito.please.core.api.DescribedOperation;
import com.atoito.please.core.api.Operation;
import com.atoito.please.core.api.OpsFile;
import com.atoito.please.test.stub.StubRegistry;
import com.atoito.please.test.util.Constants;
import com.google.common.io.Resources;

public class OpsFileTest {

    @Test(description = "ops file is loaded")
    public void testSimple() {
        StubRegistry registry = new StubRegistry();
        OpsFile opsFile = new OpsFile(Resources.getResource(Constants.PATH_OPS_FILE_SIMPLE), registry.getActionsDefinitions());
        List<Operation> operations = opsFile.getOperations();
        assertThat(operations).as("operations").isNotNull().isNotEmpty().hasSize(1);
        Operation operation = operations.get(0);
        assertThat(operation).as("operation").isNotNull().isInstanceOf(DescribedOperation.class);
		DescribedOperation describedOperation = (DescribedOperation) operation;
        assertThat(describedOperation.getId()).as("operation id").isEqualTo("ops-test-simple-01");
        assertThat(describedOperation.getDescription()).as("operation description").isEqualTo("a simple description");
    }
}
