Please
======

P.O.C. for an operation performer, usable for:

* devops
* common operations in your box (backup etc)


Why Please?
-----------

To leverage some code and 'cause I'm working in a win based / very close environment...

Please helps me to automate some common operation.


One minute test
---------------

    git clone git://github.com/enr/please.git
    gradle installApp
    ./modules/cli/target/install/please/bin/please reports

    
How it works
------------

Please works in terms of:

* operation: anything to do to achieve a goal
* action: the single step to perform operation
* ops-file: a file written using a DSL containig operations to perform

Actions help user to define operations without need to write actual code.

If you are a *nix guy, think to operation like a simple shell script and action as the single command;
indeed Please gives you actions like: `mkdir`, `exec`, etc. 

The standard way to run Please is

    please <operation>

where `<operation>` is the identifier for the operation to perform.

Operation is taken from a operation registry containing any operation:

* built-in in the main Please distribution
* defined from plugins
* defined in ops-file

Please is runned with:

    $ please <opid> [args]

this command performs operation 'opid' passing any option.

    $ please <ops-file>   # [NOT YET IMPLEMENTED]

this command (sorry, not yet implemented) performs the operation defined as 'default' in a given ops file.

Please looks for the required operation, in:

* plugins: ie jar files dropped in plugins/ directory, which expose operations as java classes implementing `com.atoito.please.core.api.Operation` and declared in META-INF/plugin-definitions.please
* ops-file


Usage
-----

By default Please offers you some operation, like 'reports' or 'actions'

To define more operations you have to code a new plugin or write a ops-file.

Ops is a file written in Please DSL located in some directory known to Please.

In this file you can define a operation as a sequence of some action, ie a 'install-tomcat' operation will be defined as:

    operation {
        id 'install-tomcat'
        description = 'this operation installs tomcat in /opt/tomcat'
        download {
            url = 'http://...mirror/tomcat.zip'
            name = 'tomcat.zip'
        }
        unzip {
            file = tomcat.zip
            destination = '/opt'
        }
        setenv {
            name = 'CATALINA_HOME'
            value = '/opt/tomcat'
        }
        setenv {
            name = 'PATH'
            value = '/opt/tomcat/bin'
            //append = true [NOT YET IMPLEMENTED]
        }
    }

You have some sample file in `modules/cli/src/main/dist/conf/ops.d`

to see which operations you can run:

    bin/please operations

to have a complete report about your actual Please installation:

    bin/please reports
    

This operation gives you a full report about current Please installation:

* paths where ops files are loaded from
* executable operations and actions
* loaded ops files
* used classpath (useful for debugging)

To have details:

    bin/please operations
    bin/please actions
    bin/please paths
    bin/please ops
    bin/please classpath

To try some operation, check paths and run:

    bin/please $operation

To see what Please would do, without actual perform it, you can run please with `--noop` (no operation) flag.

A bit more verbose logging will be written in `$PLEASE_HOME/logs/` directory.


Ops files
---------

Ops-file is a configuration file (you can think to an Ant build.xml) written using Please domain specific language.

It contains one or more operations definitions.

Every time Please is runned, it looks for ops files in default locations adn parses them.

Default locations for ops files (in order they are scanned from ops loader):

* `$PLEASE_HOME/conf/ops.d` : installation level.
* `/etc/please/ops.d` : system level for unix-like os.
* `c:\please\ops.d` : system level for win os.
* `$HOME/.please/$version/ops.d` : user level.


Please DSL
----------

Directory `modules/cli/src/main/dist/conf/ops.d` contains some sample file.
Directory `modules/acceptance-tests/src/test/ops` contains files used in tests.

Utility methods

Other than operations definitions, in ops file you can use some utility method such as:

* `String date(String format)`: returns string using SimpleDateFormatter format (default: "yyyyMMddHHmm")


Sample operations
-----------------

A sample operation to create a zip archive for Please sources:

    operation {
        id 'zip-please'
        description = 'build Please zip archive'
        zip {
            source = '/path/to/sources/please'
            destination = "/tmp/please-sources-${date()}.zip"
            excludes = '**/gradle.properties,**/.classpath,**/.project,**/.settings/**,**/target/**,**/test-output/**,*.log'
        }
    }

Grails install on win:

    operation {
        id 'install-grails'
        description = 'installs grails framework on a windows machine'
        def baseDir = /D:\dev\grails/
        def zipPath = 'd:/dev/grails-2.0.0.M2.zip'
        mkdir {
            directory = "${baseDir}"
        }
        download {
            url = 'http://dist.springframework.org.s3.amazonaws.com/milestone/GRAILS/grails-2.0.0.M2.zip'
            destination = "${zipPath}"
        }
        unzip {
            archive = "${zipPath}"
            destination = "${baseDir}"
        }
        setenv {
            name = 'GRAILS_HOME'
            value = "${baseDir}${File.separator}grails-2.0.0.M2"
        }
    }

You can use variables, and special methods (such as "date()"):

    def today = date()
    operation {
        id 'create-dir-tree-using-date'
        def baseDir = '/tmp/base/dir'
        mkdir {
            directory = "${baseDir}/create-dir-tree-using-a-var-${today}"
        }
        mkdir {
            directory = "${baseDir}/create-dir-tree-using-date-"+date()
        }
        mkdir {
            directory = "${baseDir}/01/02/03/04"
        }
    }    


    
Built in actions
----------------

You can see available actions running
    
    please actions

Append content to a file:

    append {
        file = '/tmp/myfile.txt'
        content = 'append!!'
    }


Copy a file or a directory

The `checkFrom` option is to verify if the original file exists when Please starts to perform involved operations;
as checks are executed before the actual operation, if the file to copy is created from a former action, you should
set checkFrom to false to avoid validation errors.

    copy {
        from = /C:\path\to\Very-Enterprise-2.0.ear/
        to = releaseDir
        checkFrom = false
    }

Delete file or directory:
    
    delete {
        path = "${zipDir}/static-zipped.zip"
    }

Download a file:

    download {
        url = 'http://usefuldomain/usefuljar-0.3.jar'
        destination = 'hello-download-bin.jar'
    }

Exec a command:

    exec {
        def desc = 'deploy-weblogic'
        command = 'cmd /c deploy-weblogic-with-static.bat'
        workingDirectory = /c:\projects\my/
        // comment out if you don't want to use files for standard and error output
        stdOutputFile = "${desc}-output.txt"
        errOutputFile = "${desc}-output.txt"
        // show output?
        show = true
    }

Create a directory (Please's mkdir is actually a `mkdir -p`):
    
    mkdir {
        directory = "${unzipDir}"
    }

Set environment variable:


    setenv {
        name = 'GRAILS_HOME'
        value = "${baseDir}${File.separator}grails-2.0.1"
    }

Create an archive file or expand it:

    zip {
        source = 'd:/export'
        destination = "d:/tmp/export-${date()}.zip"
        //includes = 'please/**'
        excludes = '**/.classpath,**/.project,**/.settings/**,*.log'
    }
    unzip {
        archive = "${zipDir}/static-zipped.zip"
        destination = "${unzipDir}"
    }
    targz {
        source = 'd:/teamsys/branch_1.0.4.x/webapp-anag/src/main/webapp/static'
        destination = "${zipDir}/static-tarred.tgz"
    }
    untargz {
        archive = "${zipDir}/static-tarred.tgz"
        destination = "${unzipDir}"
    }



System properties
-----------------

If Please has to work using system properties, you can write a file named please.properties in `$(pwd)` or in `$HOME/.please/$version/` .

Properties starting with `system.` are loaded with `System.setProperty` using the key without the 'system.' prefix.

A sample setting for proxy usage:

    system.http.proxySet    = true
    system.http.proxyHost   = 192.168.1.222
    system.http.proxyPort   = 3128
    system.https.proxySet   = true
    system.https.proxyHost  = 192.168.1.222
    system.https.proxyPort  = 3128


This and that
-------------

Paths:

if you are trapped in a win box, you can use paths normalized to '/', (ie c:/programs/woot) 
or use backslash in a 'slashed' string (ie `/c:\projects\my/`).


Development
-----------

Please uses Gradle as build system;

To create Please app in ./modules/cli/target/install/please/:

    ./gradlew installApp

To create the distribution zip in ./modules/cli/target/distributions/:

    ./gradlew distZip

To build a local install and run user acceptance tests:

    ./gradlew uat

To install the zip, you need only to:

    unzip modules/cli/target/distributions/please-$VERSION.zip -d /installation/path

To create Eclipse IDE files allowing you to import as "Existing Projects in Workspace" from directory modules/:

    ./gradlew eclipse

To add license headers to new files:

    ./gradlew license

To create website, Please uses Xite <http://enr.github.com/xite/index.html>

    /opt/xite/bin/xite -s website

Special dirs for distribution:

* `modules/cli/src/main/dist` will be copied in `$PLEASE_HOME`

Test results:

* [module core](/please/developers/tests/core/index.html)
* [module acceptance](/please/developers/tests/uat/index.html)


Extending Please
----------------

To programmatically extend Please, you can write a plugin.

A plugin is a jar file located in `$PLEASE_HOME/plugins`.

You can create your operations and actions implementing `com.atoito.please.core.api.Operation` and `com.atoito.please.core.api.Action`

Please offers you some utility class to extend:

* `com.atoito.please.core.api.AbstractAction`
* `com.atoito.please.core.api.AbstractProvidedOperation`

To define operations and actions (ie tell Please that some new operation/action exists) you have to create a "definition-file" located in `META-INF/plugin-definitions.please`.


Sample content for a definition file:

    actions = [ 'copy':'com.atoito.please.core.actions.CopyAction',
                'zip':'com.atoito.please.core.actions.ZipAction',
                'exec':'com.atoito.please.core.actions.ExecAction',
                'mkdir':'com.atoito.please.core.actions.MkdirAction',
                'download':'com.atoito.please.core.actions.DownloadAction',
                'setenv':'com.atoito.please.core.actions.SetEnvVariableAction' ]
    operations = [ 'reports':'com.atoito.please.core.operations.PleaseReportOperation' ]


If operation has to know the 'id' operation has been defined in registry (ie the built in Please reports operation that has different 
behaviour depending on the call is for 'reports' or 'operations' or 'actions') has to implement IdAwareOperation.

If action creates or uses files it can implement OutputsAware; this way, it receives a list of files registered as output from former actions.
Abstract action already implements it.
You can look at ZipAction for an output production/consumption example.

If action has to know additional options it has to implement `ArgsAwareAction`.

When Please is invoked using the `--noop` option, operation and actions are described using their method `String toHuman()`;
You can use the utility class `DescriptionBuilder` to build the descriptive string.


Acceptance testing
------------------

Sources for acceptance tests are in acceptance-tests module.

To create acceptance tests for actions and operations you can extend `com.atoito.please.uat.BaseUat` class.

To run existent user acceptance tests:

    ./gradlew uat


Extending DSL
-------------

To extend Please DSL you can see at `com.atoito.please.dsl.DateAbility`, introducing a `date()` method.


Download
--------

Download sources as zip: <https://github.com/enr/please/zipball/master>

Download sources as tgz: <https://github.com/enr/please/tarball/master>


License
-------

Licensed under the Apache License, Version 2.0 (the "License");
you may not use Xite except in compliance with the License.

You may obtain a copy of the License at

   <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.

