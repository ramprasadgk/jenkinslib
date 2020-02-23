def call(script, BSI_TOKEN) {
    fodStaticAssessment bsiToken: "${BSI_TOKEN}",
                        entitlementPreference: 'SingleScanOnly',
                        inProgressScanActionType: 'DoNotStartScan',
                        personalAccessToken: '{AQAAABAAAAAQ1vnXFKA+bak4pr6xme8GS4AaguPGi/u6VRoo02DfWfU=}',
                        remediationScanPreferenceType: 'RemediationScanIfAvailable',
                        srcLocation: "${script.DOTNET_TARGET}",
                        tenantId: '{AQAAABAAAAAQYcHWMc+/EglTa4HFQJ2JiGh19TtY9Sw30aluy5PwD7w=}',
                        username: '{AQAAABAAAAAQmU2X1xSFTVVIVh6r6wt07rpa+ywn5yP354ymIRjcXUQ=}'
    fodPollResults bsiToken: "${BSI_TOKEN}",
                   clientId: '{AQAAABAAAAAQdZUArDD4p1bui89GQIoc4oNETwL0eWAcLRti72asXNE=}',
                   clientSecret: '{AQAAABAAAAAQanO1y/U0I3fzxCV/1u3FHxsGuKruiZ+1RsHmkfgn1Pk=}',
                   personalAccessToken: '{AQAAABAAAAAQH0SuZ47LPkdtRkP2NxhgV1M4jHpc7jfzMQjqhVXSYbg=}',
                   pollingInterval: 1,
                   tenantId: '{AQAAABAAAAAQ+91jvIXM6RT+u3krgqFBQJr8RdaDJ42XOadKxj1r+zE=}',
                   username: '{AQAAABAAAAAQ0RO6WySW2wOZSLQvmcgzpusI42BiD08/UBdUOJTW4Z4=}'
}
