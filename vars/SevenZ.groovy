def call(String ARGUMENTS) {
   SEVENZ = tool '7z'
   STDOUT = bat label: "Running 7zip with arguments=${ARGUMENTS}",
                 returnStdout: true,
                 script: """ "${SEVENZ}" ${ARGUMENTS}"""
    echo STDOUT
}