
group 'please'
name 'uat'
version '0.1'
description '''User acceptance tests 01 Ops file'''

def projectDir = new File('.')
def buildDir = new File('target')
def baseDir = new File(buildDir, 'dir-01')


operation {
	id 'create-dir'
	def basePath = baseDir.getAbsolutePath()

	mkdir {
		directory = "${basePath}/001"
	}
	mkdir {
		directory = "${basePath}/002"
	}
	mkdir {
		directory = "${basePath}/003-"+date()
	}
	mkdir {
		directory = "${basePath}/003-"+date('yyyy')
	}
}

operation {
	id 'delete-dir'
	def basePath = baseDir.getAbsolutePath()

	delete {
		path = "${basePath}"
	}
}

operation {
	id 'empty-op'
}

operation {
	id 'sequence-operation'
	def dataSrcDir = "${projectDir.getAbsolutePath()}/src/test/data/01"
	def dataDir = "${buildDir.getAbsolutePath()}/sequence-operation-data"
	def archiveDir = "${buildDir.getAbsolutePath()}/sequence-operation-arch"
	def expandedDir = "${buildDir.getAbsolutePath()}/sequence-operation-exp"
	def archiveName = 'sequence-operation-archive.tgz'
    copy {
        from = "${dataSrcDir}"
		to = "${dataDir}"
    }
	mkdir {
		directory = "${archiveDir}"
	}
	mkdir {
		directory = "${expandedDir}"
	}
	targz {
		source = "${dataDir}"
		destination = "${archiveDir}/${archiveName}"
	}
	untargz {
		archive = "${archiveDir}/${archiveName}"
		destination = "${expandedDir}"
	}
}

operation {
	id 'template-operation'
	def dataSrcDir = "${projectDir.getAbsolutePath()}/src/test/data/01"
    template {
        source = "${dataSrcDir}/template-01.txt"
        destination = "${buildDir.getAbsolutePath()}/template-01-result.txt"
        tokens = [name:'Please', site:'GitHub.com']
    }
}
