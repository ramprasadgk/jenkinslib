def call(body) {
  	def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    pipeline {
        agent {
            any
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
	   
			stage('Deploying the Application'){
                steps{
					script{
						if (BUILD_TYPE == "alpha"){
							Octopus_Create_Release(this, APP_NAME, APP_VERSION)
						}
						else{
							Octopus_Deploy(this, APP_NAME, APP_VERSION)
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
                    Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    Send_Email(this, config.TEAM_DISTRO)
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
                  	Push_Update_Version(this)
                    Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    Send_Email(this, config.TEAM_DISTRO)
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
                    Send_Email(this, config.DEV_DISTRO)
                }
                if (BUILD_TYPE == 'beta' || BUILD_TYPE == 'prod'){
                    Send_Email(this, config.TEAM_DISTRO)
                }
              	cleanWs()
            }
          }
        }
    }
}
