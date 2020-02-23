def call(script){
    script.APP_VERSION = readFile("${script.DOTNET_TARGET}assVersion.txt").trim()
  	echo "Current Version: ${script.APP_VERSION}"
}
