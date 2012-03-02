
group 'please'
name 'sample-01'
version '0.1'
description '''A sample Ops file'''



operation {
	id 'var-setting'
	setenv {
		name = 'CATALINA_HOME'
		value = /%PATH%;D:\New Folder/
	}
}

operation {
	id 'hello-download-txt'
	download {
		url = 'http://repo1.maven.org/maven2/com/pivotallabs/robolectric/0.9.8/robolectric-0.9.8.pom.asc.md5'
		destination = 'robolectric.md5'
	}
}

operation {
	id 'hello-append'
	append {
		file = '/tmp/myfile.txt'
		content = 'append!!'
	}
}

operation {
	id 'hello-template'
	template {
		source = 'modules/core/src/test/data/template/tpl-01-src-edge-del.txt'
		destination = 'modules/core/target/tpl-01-edge-del.txt'
		//startchar = '?'
		//stopchar = '?'
        tokens = [name:'Please']
	}
}

operation {
	id 'template-operation'
	def dataSrcDir = "modules/acceptance-tests/src/test/data/01"
	template {
		source = "${dataSrcDir}/template-01.txt"
		destination = "modules/cli/target/template-01-result.txt"
		startchar = '#'
		stopchar = '#'
		tokens = [name:'Please', site:'GitHub.com']
	}
}
