def call(script, ART_REPO = "nuget-deploy-local", APP_NAME, APP_VERSION){
    echo "Uploading to target ${ART_REPO}/${APP_NAME}/"
    echo "Uploading using pattern '${APP_NAME}.${APP_VERSION}.*'"
    rtUpload (
        serverId: 'ArtifactoryZip',
        spec: """{
                     "files": [
                          {
                              "pattern": "${APP_NAME}.${APP_VERSION}.*",
                              "target": "${ART_REPO}/${APP_NAME}/"
                          }
                     ]
                 }"""
    )
    echo "Publishing Build info: Build name = '${APP_NAME}' and Build Number = '${script.BUILD_RUN}'"
    rtPublishBuildInfo (
        serverId: 'ArtifactoryZip',
        buildName: "${APP_NAME}",
        buildNumber: "${script.BUILD_RUN}"
    )
}