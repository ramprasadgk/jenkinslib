def call(script, ART_REPO = "nuget-deploy-local", APP_NAME){
    echo "Clean old Artifacts using in the repo '${ART_REPO}/${APP_NAME}/${script.BUILD_TYPE}/'"
    Art_CLI("rt del  ${ART_REPO}/${APP_NAME}/${script.BUILD_TYPE}/ --sort-by=created --sort-order=desc --offset=5 --quiet")
}