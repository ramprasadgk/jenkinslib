def call(script){
    if (script.BRANCH_NAME == "develop"){
            script.BUILD_RUN = script.BUILD_NUMBER
        	return "alpha"
    	}
    if(script.BRANCH_NAME.contains("alpha")){
            script.PREV_ENV = 'alpha'
            script.BUILD_RUN = script.BRANCH_NAME.findAll( /\d+/ )[0]
      		return "beta"
    	}
    if (script.BRANCH_NAME.contains("beta")){
            script.PREV_ENV = 'beta'
            script.BUILD_RUN = script.BRANCH_NAME.findAll( /\d+/ )[0]
        	return "prod"
    	}
    if (script.BRANCH_NAME.contains("prod")){
            script.BUILD_RUN = script.BRANCH_NAME.findAll( /\d+/ )[0]
        	return "prod"
    	}
  	}
