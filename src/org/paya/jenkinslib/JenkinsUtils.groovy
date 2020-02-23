package org.paya.jenkinslib


boolean isBranchIndexingCause() { 

    def isBranchIndexing = false 

    if (!currentBuild.rawBuild) { 

      return true 

    } 

  

    currentBuild.rawBuild.getCauses().each { cause -> 

      if (cause instanceof jenkins.branch.BranchIndexingCause) { 

        isBranchIndexing = true 

      } 

    } 

    return isBranchIndexing 

  } 

 
return this;
