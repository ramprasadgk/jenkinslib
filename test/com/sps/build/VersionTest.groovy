package com.sps.build

import org.custommonkey.xmlunit.*

class VersionTest extends GroovyTestCase {

  void testIncrementPatch() {
    Version v = new Version();
    String result = v.incrementPatch("1.0.2")
      assertEquals("Resulting version","1.0.3",result)

  }

  void testIncrementMinor() {
    Version v = new Version();
    String result = v.incrementMinor("1.0.2")
      assertEquals("Resulting version","1.1.0",result)

  }

  void testIncrementMajor() {
    Version v = new Version();
    String result = v.incrementMajor("1.0.2")
      assertEquals("Resulting version","2.0.0",result)
  }

  void testPreserveSuffix() {
    Version v = new Version()
      String result = v.incrementPatch("1.0.2-alpha1")
      assertEquals("Resulting version","1.0.3-alpha1",result)
      result = v.incrementMinor("1.0.2-alpha1")
      assertEquals("Resulting version","1.1.0-alpha1",result)
      result = v.incrementMajor("1.0.2-alpha1")
      assertEquals("Resulting version","2.0.0-alpha1",result)
  }

  void testChangeSuffix() {
    Version v = new Version()
      String result = v.incrementPatch("1.0.2-alpha1","beta")
      assertEquals("Resulting version","1.0.3-beta",result)
      result = v.incrementMinor("1.0.2-alpha1", "beta")
      assertEquals("Resulting version","1.1.0-beta",result)
      result = v.incrementMajor("1.0.2-alpha1","beta")
      assertEquals("Resulting version","2.0.0-beta",result)

      result = v.incrementPatch("1.0.2-alpha1","")
      assertEquals("Resulting version","1.0.3",result)
  }


  void testSetNuSpecVersion() {
    Version v = new Version()
      String result = 
      v.setNuSpecVersion('<package><metadata><id>waSEVD2.0</id><version>1.0.9</version><title>waSEVD</title></metadata></package>','2.0.1')
      XMLUnit.setIgnoreWhitespace(true)
      XMLUnit.compareXML('<package><metadata><id>waSEVD2.0</id><version>2.0.1</version><title>waSEVD</title></metadata></package>',result)
  }

  void testSetNuSpecVersion_RealWorld() {
    String xml = '''<?xml version="1.0" encoding="UTF-8"?><package>
      <metadata>
      <id>$id$</id>
      <version>2.0.0</version>
      </metadata>
      </package>'''

      String expected = '''<?xml version="1.0" encoding="UTF-8"?><package>
      <metadata>
      <id>$id$</id>
      <version>2.0.3</version>
      </metadata>
      </package>'''

      String result = new Version().setNuSpecVersion(xml,"2.0.3")
      XMLUnit.setIgnoreWhitespace(true)
      if (!XMLUnit.compareXML(expected,result).identical()) {
        fail("XML is not the same!  Expected:\n${expected}\nActual:\n${result}")
      }

  }
  void testSetNuSpecVersion_File() {
    File f = new File("build/nuspec.xml")
      f.write('<package><metadata><id>waSEVD2.0</id><version>1.0.9</version><title>waSEVD</title></metadata></package>')
      XMLUnit.setIgnoreWhitespace(true)
      new Version().setNuSpecVersion(f, "2.3.8-beta")
      if (!XMLUnit.compareXML('<package><metadata><id>waSEVD2.0</id><version>2.3.8-beta</version><title>waSEVD</title></metadata></package>',f.text).identical()) {
        fail("XML is not the same!  Actual:\n${f.text}")
      }
  }

  void testGetNuSpecVersion_File() {
    File f = new File("build/nuspec.xml")
      f.write('<package><metadata><id>waSEVD2.0</id><version>1.0.9-beta</version><title>waSEVD</title></metadata></package>')
      String ver = new Version().getNuSpecVersion(f)
      assertEquals("Read version","1.0.9-beta",ver)
  }

  void testGetNuSpecVersion_String() {
    String result = new Version().getNuSpecVersion('<package><metadata><id>waSEVD2.0</id><version>1.0.9-beta</version><title>waSEVD</title></metadata></package>')
      assertEquals("Read version","1.0.9-beta",result)
  }

  void testSetSuffixString() {
    String result = new Version().setSuffix("1.0.2","alpha")
      assertEquals("Result","1.0.2-alpha",result)
      result = new Version().setSuffix("1.0.2-alpha","")
      assertEquals("Result","1.0.2",result)
      result = new Version().setSuffix("1.0.2-alpha",null)
      assertEquals("Result","1.0.2",result)
      try {
        result = new Version().setSuffix("1.0.2-alpha-beta-gamma","delta")
          fail("Expected VersionException")
      } catch (VersionException ve) {
      }
  }
}
