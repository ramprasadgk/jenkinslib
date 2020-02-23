package org.paya.jenkinslib


String getCommitId() {
    String commitId = bat(returnStdout: true, script: 'git.exe rev-parse HEAD').trim()
    return commitId;
}

return this;
