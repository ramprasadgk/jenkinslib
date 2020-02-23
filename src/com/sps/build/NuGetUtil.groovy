package com.sps.build;
import com.cloudbees.groovy.cps.NonCPS;
import java.io.*


// Set the pre-release version without changing the rest
// Increment the patch version
// Restore
// Pack


class NuGetUtil implements Serializable {

  def steps
  def script

  public NuGetUtil(script) {
    this.script = script
    this.steps = script.steps
  }

  void restore(String solutionFile) {
    this.steps.bat "nuget restore \"${solutionFile}\""
  }

  String getNuspecFile(String projFile) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    return proj.nuspecFile
  }

  String getVersion(String projFile) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    return proj.getVersion()
  }



  String getAssemblyName(String projFile) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    return proj.assemblyName
  }

  String pack(String projFile) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    proj.pack()
  }

  String removeSuffix(String projFile) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    proj.removeSuffix()
    proj.flush()
  }

  String setSuffix(String projFile, String suffix) {
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    proj.setSuffix(suffix)
    proj.flush()
  }

  String sanitizeBranchName(String branchName) {
    branchName.replaceAll('/',"-").replaceAll(' ',"-")
  }

  void incrementPatch(String projFile) {
    Version v = new Version()
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    proj.incrementPatch()
    proj.flush()
  }

  void push(String projFile) {
    Version v = new Version()
    NuGetProject proj = new NuGetProject(this.script, projFile)
    proj.init()
    proj.push()
  }
}
