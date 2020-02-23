import com.sps.build.*


	node('windows') {
		ws("${env.JOB_NAME}") {
			String[] projectFiles = config.projectFiles
			bat 'set'
			boolean releaseBuild = (env.BRANCH_NAME == 'master' || env.BRANCH_NAME.startsWith("release/"))

			// some block
			stage('Checkout') {
				checkout([$class: 'GitSCM', 
				branches: [[name: env.BRANCH_NAME]], 
				doGenerateSubmoduleConfigurations: false, 
				extensions:
				[[$class: 'WipeWorkspace'],
				[$class: 'UserExclusion', excludedUsers: 'jenkins'],
				[$class: 'LocalBranch', localBranch: env.BRANCH_NAME]],
				submoduleCfg: [], 
				userRemoteConfigs: scm.userRemoteConfigs])

				def workspace = pwd()
				mf = manifest.load()

			}


			stage('Build') {
				if (releaseBuild) {
					manifest.setPreRelease(mf)
				} else {
					manifest.setPreRelease(mf, nuget.sanitizePreRelease(env.BRANCH_NAME))
				}
				msbuild.buildSolution(mf.solution)
			}

			stage('Test') {
			}

			stage('Publish') {
				try {

					manifest.packageAndPublish(mf)

					if (releaseBuild) {
						String tagName = manifest.getTagName(mf)
						bat "git tag ${tagName}"
						manifest.incrementPatchVersion(mf)

						// Commit the changes
						bat 'git config --global user.name "Jenkins"'
						bat 'git config --global user.email "Jenkins@sage.com"'
						bat 'git add -u'
						bat 'git commit -m "Updating to next release version"'
						def scmUrl = scm.getUserRemoteConfigs()[0].getUrl()
						print "Remote URL: ${scmUrl}"

						withCredentials([usernamePassword(credentialsId: 'jenkins_bitbucket', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
							String url = new UrlHelper().setCredentials(scmUrl, env.GIT_USERNAME, env.GIT_PASSWORD)
							bat("git push ${url} ${env.BRANCH_NAME} --tags --verbose")
						}
					}
				} catch (publishError) {
					print "Publish Error: ${publishError}"
					throw publishError
				}
			}
		} 
	}

