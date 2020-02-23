def call(script) {
    echo "Reading File: ${script.WORKSPACE}\\${script.DOTNET_TARGET}msbuild.settings.xml"
    def xml = readFile ".\\${script.DOTNET_TARGET}msbuild.settings.xml"
    def Project = new XmlParser().parseText(xml)
    script.APP_NAME = Project.PropertyGroup.SolutionName.text().replace(".sln", "").replace(" ", "").replace("..\\", "").replace("Deploy", "").replace("../", "")
    echo """Setting APP_NAME = ${script.APP_NAME}"""
    script.PROJECT_TYPE = Project.PropertyGroup.ProjectType.text().toLowerCase()
    echo "Setting PROJECT_TYPE = ${script.PROJECT_TYPE}"
}
