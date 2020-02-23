def call(script, APP_NAME, APP_VERSION) {
    //This module should only be Ran in the case that the you are deploying to Dev
    //If you are in need of just a deployment run Octopus_Deploy.groovy
    echo "Creating Octopus Release for version ${APP_VERSION} of ${APP_NAME}..."
    STDOUT = build job: "DevOps Utilities/Octopus - Create Release",
                   parameters: [string(name: 'octopus_project', value: "${APP_NAME}"),
                                string(name: 'release_version', value: "${APP_VERSION}")]
     echo STDOUT.getResult()
}