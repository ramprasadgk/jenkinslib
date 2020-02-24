def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    pipeline {
	    agent {
	    	'any'
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
        
            stage('GIT download .NET shared library '){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                    script{
                        
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

