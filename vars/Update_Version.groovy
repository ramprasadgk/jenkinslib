def call(script){
    BB_URL = script.GIT_URL.replace('bitbucket', 'jenkins@bitbucket')
    GIT_HOME = tool 'win-git'
  	script.BUILD_RUN = BUILD_NUMBER
  	echo "Updating ${script.DOTNET_TARGET}assVerssion.txt"
    MSBuild("${script.DOTNET_TARGET}msbuild.proj /t:incrementversion;version")
    script.APP_VERSION = readFile("${script.DOTNET_TARGET}assVersion.txt").trim()
  	return script.APP_VERSION
}
