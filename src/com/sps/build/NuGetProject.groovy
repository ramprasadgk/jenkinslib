package com.sps.build;
import com.cloudbees.groovy.cps.NonCPS;
import java.io.*


// Stores the info about a NuGet project 
// that we need for the build
class NuGetProject implements Serializable {
  static String MSBUILD_VERSION = "14.0"
  static String CREDENTIALS_ID = "jenkins_artifactory_key"
  static String REPO_URL = "http://192.168.8.162/artifactory/api/nuget/nuget-local"

  def steps
  def script
  String projFile
  String basename
  String nuspecFile
  String nuspec
  String assemblyName
  String dir

  public NuGetProject(script, String projFile) {
    this.script = script
    this.steps = script.steps
    this.dir = script.steps.pwd()
    this.projFile = "${dir}\\${projFile}"
  }

  void init() {
    //def parser = new XmlSlurper()
    //parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
    if (!this.steps.fileExists(projFile)) {
      this.steps.error "File does not exist: ${projFile}"
    }
    String projXML = this.steps.readFile(projFile).trim()
    this.basename = projFile.take(projFile.lastIndexOf("."))
    this.nuspecFile = basename + ".nuspec"
    if (!this.steps.fileExists("${nuspecFile}")) {
      this.steps.error "Could not read nuspec file ${nuspecFile}"
    }
    this.nuspec = this.steps.readFile(nuspecFile)
    this.nuspec = removeBOM(nuspec)
    this.steps.echo "Read nuspec:\n${nuspec}" 

    def nuspecXML = new XmlSlurper(false,false).parseText(nuspec)
    this.assemblyName = nuspecXML.metadata.id.text()
    if (assemblyName == null || assemblyName == "" || assemblyName == "\$id\$") {
      def projDoc = new XmlSlurper(false, false).parse(new ByteArrayInputStream(projXML.getBytes()))
      assemblyName = projDoc.PropertyGroup[0].AssemblyName.text()
      if (assemblyName == null || assemblyName == "") {
        this.steps.error "Could not detremine assembly name from project ${projFile}"
      }
    }
  }

  void flush() {
    this.steps.writeFile(file: nuspecFile, text: nuspec)
  }

  void incrementPatch() {
    Version v = new Version()
    String currVer = v.getNuSpecVersion(nuspec)
    this.nuspec = v.setNuSpecVersion(this.nuspec, v.incrementPatch(currVer))
  }

  String getVersion() {
    return new Version().getNuSpecVersion(this.nuspec)
  }

  void push() {
    String version = getVersion()
    String packageName = this.assemblyName + "." + version + ".nupkg"
    steps.withCredentials([this.steps.usernamePassword(credentialsId: CREDENTIALS_ID, passwordVariable: 'ART_PASSWORD', usernameVariable: 'ART_USERNAME')]) {
      this.steps.bat "nuget push ${packageName} -Source ${REPO_URL} -ApiKey ${this.script.env.ART_USERNAME}:${this.script.env.ART_PASSWORD}"
    }
  }

  void setSuffix(String suffix) {
    Version v = new Version()
    String currVer = v.getNuSpecVersion((String)this.nuspec)
    String newVer = v.setSuffix(currVer, suffix)
    this.nuspec = v.setNuSpecVersion(this.nuspec, newVer)
  }

  String pack() {
    this.steps.bat "nuget pack \"${projFile}\" ${this.commonArgs}"
  }

  String getCommonArgs() {
    return "-MSBuildVersion 14.0"
  }

  void removeSuffix() {
    setSuffix(null)
  }

  /** Remove unicode byte order marker to avoid XML parse error */
  String removeBOM(String raw) {
    byte[] bytes = raw.getBytes()
    byte[] lookinFor = [0xef,0xbb,0xbf]
    byte[] firstThree = bytes[0..2]
    Writable asStr = bytes.encodeHex()

    String toRet

    if (firstThree == lookinFor) {
      byte[] remaining = bytes[3..bytes.length-1]
      toRet = new String(remaining)
    } else {
      toRet = raw
    }
    asStr = toRet.getBytes().encodeHex()
    return toRet
  }
}

