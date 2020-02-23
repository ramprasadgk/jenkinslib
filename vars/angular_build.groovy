def call(body) {:wq
  	def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    pipeline {
        agent {
            label 'windows'
        }
        options {
            disableConcurrentBuilds()
        }
        environment {
		  BUILD_TYPE = Get_Build_Type(this)
          APP_NAME = " "
          PROJECT_TYPE = " "
          DOTNET_TARGET = " "
          OUTPUT_FOLDER = "publish"
		  PACKAGE_EXCLUSIONS = " "
        }
        stages{
	    stage('check for branch indexing'){
	    	steps{
		    scripts{
		    	if (new JenkinsUtils().isBranchIndexingCause()){ 

                    	    currentBuild.result = 'ABORTED' 

                            error('Trigger caused due to branch indexing, skipping the build...') 
	                    } 
		    }
		}

	    }
            stage('GIT download .NET shared library '){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                    script{
                        BUILD_RUN = BUILD_NUMBER
                      if (config.DOTNET_TARGET!=null){
                        DOTNET_TARGET = config.DOTNET_TARGET
                        echo "Using this location for the packaging ${DOTNET_TARGET}"
                        Dotnet_Clone(this)
                      }
                      else{
                        DOTNET_TARGET = ''
                        echo "Cloning DotNet repo into the root of the workspace"
                        Dotnet_Clone(this)
                      }
                    }
                }
            }
	   }
        post {
          unstable {
            script{
                if (BUILD_TYPE == 'alpha'){
                    Push_Update_Version(this)
                    //Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    //Send_Email(this, config.TEAM_DISTRO)
                }
                if (!(BRANCH_NAME =~ /prod.*/)){
                    Create_Branch(this)
                    Delete_Branch(this)
                }
              	cleanWs()
            }
          }
          success {
            script{
                if (BUILD_TYPE == 'alpha'){
                    //Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    //Send_Email(this, config.TEAM_DISTRO)
                }
                if (!(BRANCH_NAME =~ /prod.*/)){
                    Create_Branch(this)
                    Delete_Branch(this)
                }
              	cleanWs()
            }
          }
          failure {
            script{
                if (BUILD_TYPE == 'alpha'){
                    //Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    //Send_Email(this, config.TEAM_DISTRO)
                }
              	cleanWs()
            }
          }
        }
    }
}
