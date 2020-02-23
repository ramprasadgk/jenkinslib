def call(script, APP_NAME, APP_VERSION) {
    //This module will deploy a version of an application to a given environment.
    echo "Starting Octopus Deployment of version ${APP_VERSION} of ${APP_NAME} into the ${script.BUILD_TYPE} environment..."
    STDOUT = build job: "DevOps Utilities/Octopus - Deploy",
          parameters: [string(name: 'octopus_project', value: "${APP_NAME}"),
                       string(name: 'octopus_environment', value: "${script.BUILD_TYPE}"),
                       string(name: 'release_version', value: "${APP_VERSION}")]
    echo STDOUT.getResult()
}