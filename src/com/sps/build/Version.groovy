package com.sps.build
import groovy.util.Node
import java.io.*
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovy.util.slurpersupport.GPathResult

class Version {

  String increment(String verStr, int index) throws VersionException {
    return increment(verStr, index, null)
  }

  String increment(String verStr, int index, String suffixStr) throws VersionException {
    // Split the patch version off from the rest of the string
    // Ex:  1.0.2-alpha becomes {"1.0.2","alpha"}
    String[] mainParts = verStr.split("-")

    // Now split major.minor.patch up
    // Ex:  1.0.2 becomes {"1","0","2"}

    String[] numericParts = mainParts[0].split("\\.")
    for (int i=0;i<numericParts.length;i++) {
      if (!numericParts[i].isNumber()) {
        throw new VersionException("Could not parse version: " + verStr + ".  Please make sure it matches the format <majorVersion>.<minorVersion>.<patchVersion>-<suffixString>")
      }
    }

    numericParts[index] = new Integer(numericParts[index])+1+""
    String toRet = numericParts[0]
		for (int i=1;i<numericParts.length;i++) {
			if (i > index) {
					toRet = toRet + ".0"
			} else {
					toRet = toRet + "." + numericParts[i]
			}
		}
    if (suffixStr == null && mainParts.length > 1) {
      toRet = toRet + "-" + mainParts[1]
    } else if (suffixStr != null && suffixStr != "") {
        toRet = toRet + "-"+suffixStr
    } 
		return toRet;
  }

  String setSuffix(String verStr, String suffix) throws VersionException {
    String[] mainParts = verStr.split("-")
      if (mainParts.length > 2) {
        throw new VersionException("Could not split " + verStr + " into SemVer parts")
      }
    if (suffix == null || suffix == "") {
      return mainParts[0]
    } else {
      return mainParts[0] + "-" + suffix
    }
  }


  String incrementPatch(String verStr) throws VersionException {
    return incrementPatch(verStr,null)
  }

  String incrementPatch(String verStr, String suffixStr) throws VersionException {
    return increment(verStr, 2, suffixStr)
  }

  String incrementMinor(String verStr) throws VersionException {
    return incrementMinor(verStr, null)
  }

  String incrementMinor(String verStr, String suffixStr) throws VersionException {
    return increment(verStr, 1, suffixStr)
  }

  String incrementMajor(String verStr) throws VersionException {
    return incrementMajor(verStr, null)
  }

  String incrementMajor(String verStr, String suffixStr) throws VersionException {
    return increment(verStr, 0, suffixStr)
  }

  String setNuSpecVersion(String nuSpecXML, String versionStr) {
    def doc = new XmlSlurper().parseText(nuSpecXML)
      setNuSpecVersion(doc, versionStr)
      return XmlUtil.serialize(doc)
  }

  void setNuSpecVersion(File nuSpecXML, String versionStr) {
    def doc = new XmlSlurper().parse(nuSpecXML)
      doc = setNuSpecVersion(doc, versionStr)
      groovy.xml.XmlUtil.serialize(doc, nuSpecXML.newPrintWriter())
  }

  String getNuSpecVersion(String nuSpecXML) {
    def doc = new XmlSlurper().parse(new ByteArrayInputStream(nuSpecXML.getBytes()))
      return getNuSpecVersion(doc)
  }


  String getNuSpecVersion(File nuSpecXML) {
    def doc = new XmlSlurper().parse(nuSpecXML)
      return getNuSpecVersion(doc)
  }

  String getNuSpecVersion(GPathResult doc) {
    return doc.metadata.version.text()
  }

  GPathResult setNuSpecVersion(GPathResult doc, String versionStr) {
    doc.metadata.version.replaceBody versionStr
      return doc
  }
}
