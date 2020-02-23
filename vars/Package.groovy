def call(script, DESCRIPTION) {
	def commitId= new GetCommitInfo().getCommitId()
	Date now = new Date();
    int year = now.getYear() + 1900;
	writeFile file: "${script.APP_NAME}.nuspec",
	          text: """<?xml version="1.0"?>
                        <package >
                          <metadata>
                            <id>${script.APP_NAME}</id>
                            <version>${script.APP_VERSION}</version>
                            <authors>Paya</authors>
                            <owners>Paya</owners>
                            <requireLicenseAcceptance>false</requireLicenseAcceptance>
                            <description>${DESCRIPTION}</description>
                            <releaseNotes>${commitId}</releaseNotes>
                            <copyright>Copyright ${year}</copyright>
                            <tags>${script.APP_NAME}.${script.APP_VERSION}</tags>
                          </metadata>
                          <files>
                            <file src="${script.OUTPUT_FOLDER}\\**" exclude="${script.PACKAGE_EXCLUSIONS}"/>
                          </files>
                        </package>
	          """
	nuget("pack ${script.APP_NAME}.nuspec")
}
