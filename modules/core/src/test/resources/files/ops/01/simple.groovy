/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2012 - https://github.com/enr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */


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



