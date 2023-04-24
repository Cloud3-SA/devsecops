pipeline {
    agent any

    stages {

        stage('DAST') {
            steps {
                script {
                    ZAP_API_KEY = "TesteDAST"
                    ZAP_BASE_URL = "http://IP-HOST:8090"
                    TARGET_URL = "http://IP-HOST:3000"

                    // Start a new ZAP session
                    sh "curl -s '${ZAP_BASE_URL}/JSON/core/action/newSession/?apikey=${ZAP_API_KEY}&name=session'"

                    // Access the target application
                    sh "curl -s '${ZAP_BASE_URL}/JSON/core/action/accessUrl/?apikey=${ZAP_API_KEY}&url=${TARGET_URL}&followRedirects=true'"

                    // Spider the application
                    sh "curl -s '${ZAP_BASE_URL}/JSON/spider/action/scan/?apikey=${ZAP_API_KEY}&url=${TARGET_URL}'"

                    // Run the active scan
                    sh "curl -s '${ZAP_BASE_URL}/JSON/ascan/action/scan/?apikey=${ZAP_API_KEY}&url=${TARGET_URL}&recurse=true&inScopeOnly=&scanPolicyName=&method=&postData=&contextId='"

                    // Monitor the active scan progress
                    while (true) {
                        SCAN_PROGRESS = sh(script: "curl -s '${ZAP_BASE_URL}/JSON/ascan/view/status/?apikey=${ZAP_API_KEY}'", returnStdout: true).trim()
                        echo "ZAP Scan progress: ${SCAN_PROGRESS}%"

                        if (SCAN_PROGRESS >= "100") {
                            break
                        }
                        sleep 30
                    }

                    // Generate the report and save it to a file
                    sh "curl -s '${ZAP_BASE_URL}/OTHER/core/other/htmlreport/?apikey=${ZAP_API_KEY}' > zap_report.html"
                }
            }  
        }  
    }
}        