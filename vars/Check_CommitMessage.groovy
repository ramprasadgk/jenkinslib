def call(Map args) {
    if (args.action == 'check') {
        return check()
    }
    error 'Check_CommitMessage has been called without valid arguments'
}

def check() {
    env.SKIP = "false"
    result = sh (script: "git log -1 | grep '.*\\[skip deploy\\].*'", returnStatus: true)
    if (result == 0) {
        env.SKIP = "true"
        error "'[skip deploy]' found in git commit message. Aborting."
	return false
    }
}

