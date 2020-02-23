def call( PS_FILENAME, baseURL, branchName , useCredentials) {

        PS_TARGETPATH = 'DL_' + UUID.randomUUID().toString() + '.' + ( PS_FILENAME - ~/.*(?<=\.)/ )
       
        FullURL = "${baseURL}/raw/${PS_FILENAME}?at=refs/heads/${branchName}"
        println "FullURL = ${FullURL}" 
        STDOUT = bat label: "Get ${PS_FILENAME} from ${baseURL} Branch:${branchName} to ${PS_TARGETPATH}",
                        returnStdout: true,
                        script:"""curl  ${FullURL} --insecure -u ${useCredentials} > ${PS_TARGETPATH} """
        echo STDOUT

    return PS_TARGETPATH

}

