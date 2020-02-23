def call(script){
    BB_URL = script.GIT_URL.replace('bitbucket', 'jenkins@bitbucket')
    GIT_HOME = tool 'win-git'
  	script.BUILD_RUN = BUILD_NUMBER
  	STDOUT = bat label: "Checking out branch ${script.BRANCH_NAME}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" checkout ${script.BRANCH_NAME}"""
    echo STDOUT
    echo "Updating ${script.DOTNET_TARGET}assVerssion.txt"
    STDOUT = bat label: "Saving version changes to ${script.BRANCH_NAME}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" add ${script.DOTNET_TARGET}assVersion.txt
                            "${GIT_HOME}" add ${script.DOTNET_TARGET}CommonAssemblyInfo.cs
                            "${GIT_HOME}" add ${script.DOTNET_TARGET}CommonAssemblyInfo.vb"""
    echo STDOUT
    STDOUT = bat label: "Pulling remote branch ${script.BRANCH_NAME}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" pull ${BB_URL}"""
    echo STDOUT
  	DT_NAME = script.DOTNET_TARGET.replace("/","").replace("\\","")
    STDOUT = bat label: "Committing ${script.BRANCH_NAME}",
                 returnStdout: true,
      script:""" "${GIT_HOME}" commit -m "${DT_NAME} Updated to version: ${script.APP_VERSION}" """
    echo STDOUT
    STDOUT = bat label: "Pushing Branch ${script.BRANCH_NAME} to ${BB_URL}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" push ${BB_URL} ${script.BRANCH_NAME}:${script.BRANCH_NAME}"""
    echo STDOUT
}
