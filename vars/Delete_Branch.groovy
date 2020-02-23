def call(script){
    def BB_URL = script.GIT_URL.replace('bitbucket', 'jenkins@bitbucket')
    GIT_HOME = tool 'win-git'
    cleanWs()
    bat label: 'Cloning Repo',
        returnStdout: true,
        script:""" "${GIT_HOME}" clone ${BB_URL} ."""
    STDOUT = bat label: 'Listing Remote Repos',
                 returnStdout: true,
                 script:""" "${GIT_HOME}" branch -r"""
    echo STDOUT
    CLEAN_STDOUT = STDOUT.replaceAll(">git branch -r", '').replaceAll('origin/HEAD -> origin/master', '').replaceAll("origin/", '')
    echo CLEAN_STDOUT
    BRANCHES = CLEAN_STDOUT.split()
  	if (script.DOTNET_TARGET!=''){
      DT_NAME = script.DOTNET_TARGET.replace("/","").replace("\\","")
      BRANCHES.each{
          if (it.contains("${DT_NAME}-${script.BUILD_TYPE}")){
            if (it.trim() != "${DT_NAME}-${script.BUILD_TYPE}+${script.BUILD_RUN}") {
                  bat label: "Deleted old Branch (${it})",
                  script:""" "${GIT_HOME}" push ${BB_URL} -d ${it}"""
              }
          }
       }
    }
    else{
      BRANCHES.each{
          if (it.contains(BUILD_TYPE)){
              if (it.trim() != "${script.BUILD_TYPE}+${script.BUILD_RUN}") {
                  bat label: "Deleted old Branch (${it})",
                  script:""" "${GIT_HOME}" push ${BB_URL} -d ${it}"""
              }
          }
       }
    }
}
