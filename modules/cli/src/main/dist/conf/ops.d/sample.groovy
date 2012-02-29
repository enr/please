
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
		source = '/path/to/template'
		destination = '/path/to/destination'
        tokens = [a:1, b:2]
	}
}
