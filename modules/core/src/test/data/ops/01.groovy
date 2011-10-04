
group 'com.atoito.please.tests'
name 'simple'
version '0.1'
description '''A simple ops file'''

operation {
    id 'ops-test-01' 
	description = 'a simple description'
    
    'action-01' {
        p01 = 'una string'
        p02 = []
        p03 = [:]
    }
}