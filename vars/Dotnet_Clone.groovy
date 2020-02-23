def call(script){
    def BB_URL = "https://jenkins@bitbucket.paya.com/scm/dev/dotnet.git"
    GIT_HOME = tool 'win-git'
    STDOUT = bat label: "Cloning Repo ${BB_URL}", 
                 returnStdout: true,
                 script:""" "${GIT_HOME}" clone ${BB_URL} dotnet"""
    echo STDOUT
  if (script.DOTNET_TARGET!=""){
  	STDOUT = bat label: "Moving Dotnet files to ${script.DOTNET_TARGET}",
                 returnStdout: true, 
      			 script: """move dotnet\\* ${script.DOTNET_TARGET}"""
    echo STDOUT
  }
  else{
    STDOUT = bat label: "Moving Dotnet files to .",
                 returnStdout: true, 
    			 script: """move dotnet\\* ."""
    echo STDOUT
  }
}
