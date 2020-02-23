package com.sps.build

class ManifestTest extends GroovyTestCase {

  void testHappyPath() {
    Manifest m = new Manifest()
    m.parse(new File("test/resources/manifest_simple.json").text)
    assertEquals("Main solution",'Main.sln',m.mainSolution)
    assertEquals("Solutions", ['module1/module1.sln','module2/module2.sln'], m.solutions)
    assertEquals("All solutions",['module1/module1.sln','module2/module2.sln','Main.sln'],m.allSolutions())
  }


  void testNoSolutions() {
    Manifest m = new Manifest()
    m.parse(new File("test/resources/manifest_no_solutions.json").text)
    assertEquals("Main solution",'Main.sln',m.mainSolution)
    assertEquals("Solutions", null, m.solutions)
    assertEquals("All solutions",['Main.sln'],m.allSolutions())
  }
}
