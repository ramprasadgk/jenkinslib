package com.sps.build

import org.custommonkey.xmlunit.*

class UrlHelperTest extends GroovyTestCase {

  void testHappyPath() {
    UrlHelper u = new UrlHelper();
    String result = u.setCredentials("http://jenkins@192.168.8.7/scm/test/repo.git","setUser","setPassword")
    assertEquals("Result","http://setUser:setPassword@192.168.8.7/scm/test/repo.git",result)
  }
}
