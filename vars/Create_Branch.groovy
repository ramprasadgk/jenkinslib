def call(script){
    def BB_URL = script.GIT_URL.replace('bitbucket', 'jenkins@bitbucket')
    GIT_HOME = tool 'win-git'
    cleanWs()
    STDOUT = bat label: "Cloning Repo ${BB_URL}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" clone ${BB_URL} ."""
    echo STDOUT
    STDOUT = bat label: "Checking out branch ${script.BRANCH_NAME}",
                 returnStdout: true,
                 script:""" "${GIT_HOME}" checkout ${script.BRANCH_NAME}"""
    echo STDOUT
  	if (script.DOTNET_TARGET!=''){
      DT_NAME = script.DOTNET_TARGET.replace("/","").replace("\\","")
      STDOUT = bat label: "Creating new Branch named ${DT_NAME}-${script.BUILD_TYPE}+${script.BUILD_RUN}",
                   returnStdout: true,
                   script:""" "${GIT_HOME}" checkout -b ${DT_NAME}-${script.BUILD_TYPE}+${script.BUILD_RUN}"""
      echo STDOUT
      STDOUT = bat label: "Pushing Branch ${DT_NAME}-${script.BUILD_TYPE}+${script.BUILD_RUN} to ${BB_URL}",
                   returnStdout: true,
                   script:""" "${GIT_HOME}" push -u ${BB_URL} ${DT_NAME}-${script.BUILD_TYPE}+${script.BUILD_RUN}"""
      echo STDOUT
    }
  	else{
      STDOUT = bat label: "Creating new Branch named ${script.BUILD_TYPE}+${script.BUILD_RUN}",
                   returnStdout: true,
                   script:""" "${GIT_HOME}" checkout -b ${script.BUILD_TYPE}+${script.BUILD_RUN}"""
      echo STDOUT
      STDOUT = bat label: "Pushing Branch ${script.BUILD_TYPE}+${script.BUILD_RUN} to ${BB_URL}",
                   returnStdout: true,
                   script:""" "${GIT_HOME}" push -u ${BB_URL} ${script.BUILD_TYPE}+${script.BUILD_RUN}"""
      echo STDOUT
  	}

}
