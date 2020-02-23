def call(body) {
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
            stage('Updating assVersion.txt'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                	script{
                    	Update_Version(this)
                    }
                }
            }
            stage('Reading Project information'){
                steps{
                  script{
                    if (config.DOTNET_TARGET==null){
                      DOTNET_TARGET = ''
                    }
                    Read_Version(this)
                    Set_Project_Settings(this)
                  }
                }
            }
            stage('Sonarqube Analysis'){
                when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                    script{
                       Sonarqube_Analysis(this, APP_NAME)
                    }
                }
            }
          	stage('Restoring Nuget dependencies'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
					script{
                      MSBuild("${DOTNET_TARGET}msbuild.proj /t:NugetR")
					}
                }
            }
			stage('Building the application'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
					script{
                      MSBuild("${DOTNET_TARGET}msbuild.proj /t:${PROJECT_TYPE}")
					}
                }
            }
            stage('Fortify Analysis'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                    Fortify_Analysis(this, config.BSI_TOKEN)
                }
            }
			stage('Packaging the application'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
                  script{
                    if (config.PACKAGE_EXCLUSIONS==null){
                      PACKAGE_EXCLUSIONS = ''
                    }
                    else{
                      PACKAGE_EXCLUSIONS = config.PACKAGE_EXCLUSIONS
                    }
                    if (config.OUTPUT_FOLDER!=null){
                      OUTPUT_FOLDER = config.OUTPUT_FOLDER
                      echo "Using this location for the packaging ${OUTPUT_FOLDER}"
                      Package(this, config.DESCRIPTION)
                    }
                    else{
                      echo "Using this location for the packaging ${OUTPUT_FOLDER}"
                      Package(this, config.DESCRIPTION)
                    }
                  }
                }
            }
			stage('Uploading the packaged application to Artifactory'){
              	when { expression {  BUILD_TYPE == 'alpha' } }
                steps{
					Artifactory_Upload(this, APP_NAME, APP_VERSION)
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
