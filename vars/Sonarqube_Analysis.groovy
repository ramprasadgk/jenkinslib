def call(script, APP_NAME) {
  	SCANNER_HOME = tool 'SonarScanner1'
    // If you have configured more than one global server connection, you can specify its name
    withSonarQubeEnv('Paya SonarQube') {
        bat label: "Validating JAVA_HOME",
            returnStdout: true, 
            script: """echo %JAVA_HOME%"""
		if (script.DOTNET_TARGET!=""){
            TARGET = "${script.WORKSPACE}\\${script.DOTNET_TARGET}"
        }
      	else{
			TARGET = "${script.WORKSPACE}"
        }
        stdout = bat label: "Execute SonarQube Analysis",
            returnStdout: true, 
            script: """${SCANNER_HOME}\\bin\\sonar-scanner -Dsonar.projectName="${APP_NAME}" -Dsonar.projectVersion="${script.APP_VERSION}" -Dsonar.projectKey="${APP_NAME}" -Dsonar.sources="${TARGET}" """
        echo stdout
        // timeout(time: 1, unit: 'HOURS') {
        //       def qualitygate = waitForQualityGate()
        //       if (qualitygate.status != "OK") {
        //          error "Pipeline aborted due to quality gate coverage failure: ${qualitygate.status}"
        //       }
        // }
    }
}
