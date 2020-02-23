def call(String ARGUMENTS) {
   ART_CLI = tool 'jfrog-cli'
   STDOUT = bat label: "Running Jfrog CLI with arguments=${ARGUMENTS}",
                returnStdout: true,
                script: """ "${ART_CLI}" ${ARGUMENTS}"""
   echo STDOUT
}