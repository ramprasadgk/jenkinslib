def call(String ARGUMENTS) {
   NUGET = tool 'nuget'
   STDOUT = bat label: "Running Nuget with ${ARGUMENTS}",
                 returnStdout: true,
                 script: """ "${NUGET}" ${ARGUMENTS}"""
   echo STDOUT
}
