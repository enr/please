
group 'com.atoito.please.tests'
name 'simple'
version '0.1'
description '''A simple ops file'''

operation {
    id 'ops-test-simple-01'
	description = 'a simple description'
    
    'act-test-simple-01' {
        p01 = 'una string'
        p02 = []
        p03 = [:]
    }
}