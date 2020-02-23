def call(script, recipient){
    def emailSubject = "${JOB_NAME} - Build# ${script.BUILD_RUN} - Build Type ${script.BUILD_TYPE}"
    def emailBody = '${SCRIPT, template="groovy-html.template"}'
    echo recipient
    emailext(
        subject: emailSubject,
        mimeType: 'text/html',
        to: "${recipient}, IT_DevOps_Team@paya.com",
        attachLog: true,
        body: emailBody
    )
}