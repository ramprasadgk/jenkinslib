def call(script) {
    bat label: "Deleting Base web.config",
        script:"""del web.config"""
    echo """Setting web.config files"""
    ENVS = ['alpha','beta', 'prod']
    ENVS.each{x ->
        configFileProvider([configFile(fileId:x, targetLocation: "Web.${x}.Config", variable: 'CONFIG_FILE')]) {
            DATA = readFile(CONFIG_FILE)
            echo "===================================================================== Begin of Web.config for ${x} =============================================================================="
            echo DATA
            echo "===================================================================== End of Web.config for ${x} ================================================================================"
        }
    }
}     