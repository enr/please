
app {
    version = "0.1-SNAPSHOT"
    baseContext = '/please'
    // set to null to use platform default
    encoding = 'UTF-8'
}

// you can override default value for destination directory
project {
    source = 'website'
    destination = 'target/xite/please'
}

plugins { 
    enabled = [ 'markdown',
                'resources']
}

markdown {
    extensions = ['md']
    code {
        template = '<script type="syntaxhighlighter" class="brush: %s"><![CDATA[%s]]></script>';
    }
}

resources {

    sources {
        additionals = [ '/opt/syntaxhighlighter/3.0.83':'', 
                        'modules/acceptance-tests/target/reports/tests':'developers/tests/uat', 
                        'modules/core/target/reports/tests':'developers/tests/core', 
                        'modules/cli/target/reports/tests':'developers/tests/cli']
    }
    excludedFilenameSuffix = ['~']
}

