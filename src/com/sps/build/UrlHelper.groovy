package com.sps.build;

import java.net.URL

class UrlHelper {

  String setCredentials(String rawUrl, String username, String password) {
    URL url = new URL(rawUrl)
    URI result = new URI(url.getProtocol(), "${username}:${password}", url.getHost(), url.getPort(), url.getPath(),url.getQuery(), url.getRef())
    return result.toURL().toExternalForm()
  }
}
