package com.sps.build
import groovy.json.*

class Manifest {

  String solution = ""
  String tagBase = ""
  List packages = []

  void parse(String xml) {
    def parsed = new JsonSlurper().parseText(xml)
    this.solution = parsed.solution
    this.tagBase = parsed.tagBase
    this.packages = parsed.packages
  }

  void validate() {
    // Need at least a mainSolution and tagBase
    if (tagBase == null || solution == null) {
      throw new RuntimeException("At least solution and tagBase are required")
    }
  }
}
