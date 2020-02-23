def call(String ARGUMENTS) {
   MSBUILD = tool 'MSBuild'
   STDOUT = bat label: "Running MSBUILD with ${ARGUMENTS}",
                 returnStdout: true,
                 script: """ "${MSBUILD}" ${ARGUMENTS}"""
   echo STDOUT
}

