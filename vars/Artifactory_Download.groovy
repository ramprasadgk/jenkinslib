def call(script, ART_REPO = "nuget-deploy-local", APP_NAME){
    echo "Downloading using pattern '${ART_REPO}/${APP_NAME}/${script.PREV_ENV}/*+${script.BUILD_RUN}.*'"
    rtDownload (
        serverId: 'ArtifactoryZip',
        spec: """{
                     "files": [
                         {
                             "pattern": "${ART_REPO}/${APP_NAME}/${script.PREV_ENV}/*+${script.BUILD_RUN}.*",
                             "flat": "true"
                         }
                     ]
                 }"""
    )
}