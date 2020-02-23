import com.sps.build.*

def load(String file="manifest.json") {
	try {
		Manifest mf = new Manifest()
		def manifestText = readFile 'manifest.json'
		mf.parse(manifestText)
		mf.validate()
		return mf
	} catch (error) {
		print "Error: ${error}"
		throw error
	}
}

def setPreRelease(Manifest mf, String preReleaseStr="") {
	for (projFile in mf.packages) {
		// First remove any pre-release suffix string
		nuget.setPreRelease(projFile, preReleaseStr)
	}
}

def packageAndPublish(Manifest mf) {
	for (projFile in mf.packages) {
		// Now pack using the release version
		print "Packing"
		nuget.pack(projFile)

		print "Pushing"
		// Push the results
		nuget.push(projFile)
	}
}

def getTagName(Manifest mf) {
	String assemblyVersion = nuget.getVersion(mf.tagBase)
	String assemblyName = nuget.getAssemblyName(mf.tagBase)
	String tagName = "${assemblyName}-${assemblyVersion}"
	return tagName
}

def incrementPatchVersion(Manifest mf) {
	for (projFile in mf.packages) {
		nuget.incrementPatchVersion(projFile)
	}
}
