package com.sps.build

class VersionException extends Exception {
  public VersionException(Throwable cause) {
    this(null, cause)
  }

  public VersionException(String message) {
    this(message, null)
  }

  public VersionException(String message, Throwable cause) {
    super(message, cause)
  }
}
