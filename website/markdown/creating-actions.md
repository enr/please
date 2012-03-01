
Choose the DSL, creating a operation in sample.groovy

    operation {
        id 'hello-template'
        template {
            source = '/path/to/template'
            destination = '/path/to/destination'
            tokens = [a:1, b:2]
        }
    }

Add the action in definitions file:

    'template':'com.atoito.please.core.actions.TemplateAction'

Create a basic implementation;
Fields in DSL will be class' fields.

    import com.atoito.please.core.api.AbstractAction;
    import com.atoito.please.core.util.Actions;

    public class TemplateAction extends AbstractAction {

    	private File source;
        private File destination;
        private Map<String, String> tokens;

        protected void internalExecute() {

        }

        protected void internalInitialize() {

        }

        @Override
        public String toString() {
            return this.getClass().getName() + ". " + Actions.dumpStore(store);
        }

        public String toHuman() {
            return null;
        }
    }

And a basic implementation of its unit test:

    import org.testng.annotations.BeforeClass;

    public class TemplateActionTest extends ActionTestBase {

        @BeforeClass
        public void setUp() throws Exception {
            init();
        }
    }

Ok, we are ready to start.

Let's add some validation:

    protected void internalInitialize() {
        Preconditions.checkNotNull(store);
        source = resolveAndValidateSource();
        destination = resolveAndValidateDestination();
        tokens = resolveAndValidateTokens();
    }


    private Map<String, String> resolveAndValidateTokens() {
    	Object configured = Preconditions.checkNotNull(store.get("tokens"));
    	return Store.toMap(configured);
	}

	private File resolveAndValidateDestination() {
    	Object configured = Preconditions.checkNotNull(store.get("destination"));
    	return Store.toFile(configured);
	}

	private File resolveAndValidateSource() {
    	Object configured = Preconditions.checkNotNull(store.get("source"));
    	return Store.toFile(configured);
	}
    
internalInitialize is executed at action's initialization and contains validations.

Write a test checking that validation works:

    @Test(expectedExceptions={Exception.class})
    public void testFailWithNoProperty() {
        Action action = new TemplateAction();
        action.setProperty("source", null);
        action.setProperty("destination", null);
        action.setProperty("tokens", null);
        action.initialize;
    }
    
Add UAT:

Basic implementation:

public class TemplateActionUat extends BaseUat {

    @BeforeClass
    public void init() throws Exception {

    }

    @Test(description = "uat for operation 'template-operation'")
    public void createDirectory() {
        String[] args = new String[] { "template-operation" };
        runApplicationWithArgs(args);
    }
}

Add the action under test to the ops file used in UAT (acceptance-tests/src/test/ops):


    operation {
        id 'template-operation'
        def dataSrcDir = "${projectDir.getAbsolutePath()}/src/test/data/01"
        template {
            source = "${dataSrcDir}/template-01.txt"
            destination = "${buildDir.getAbsolutePath()}/template-01-result.txt"
            tokens = [name:'Please', site:'GitHub.com']
        }
    }

Add used files, in this case template-01.txt

    Hello from <name>!
    Fork me on <site>

Write down the actual test:

    @Test(description = "uat for operation 'template-operation'")
    public void createDirectory() throws Exception {
        String[] args = new String[] { "template-operation" };
        runApplicationWithArgs(args);
        File resultFile = new File(getBuildDir(), "template-01-result.txt");
        assertThat(resultFile).as("result file").exists();
        String content = Files.toString(resultFile, Charsets.UTF_8);
        assertThat(content).as("result content").isEqualTo("Hello from n!\nFork me on s");
    }

