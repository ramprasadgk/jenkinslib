def call(String ARGUMENTS) {
   YARN = tool 'yarn'
   STDOUT = bat label: "Running yarn with arguments=${ARGUMENTS}",
                returnStdout: true,
                script: """ "${YARN}" ${ARGUMENTS}"""
   echo STDOUT
}