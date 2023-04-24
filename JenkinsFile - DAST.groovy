pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'http://IP-Host:9000'
        PROJECT_NAME = 'Unimed'
        PROJECT_VERSION = '1.0.0'
    }

        stage('DAST with OWASP ZAP') {
            steps {
                script {
                    // Set the target URL, API key and ZAP container host
                    def targetUrl = 'http://IP-Host:9090'
                    def zapApiKey = 'TesteDAST'
                    def zapHost = 'http://IP-Host:8090'

                    // Start the ZAP scan
                    sh "curl -s -X POST '${zapHost}/JSON/ascan/action/scan/?apikey=${zapApiKey}&url=${targetUrl}&recurse=true'"

                    // Check the status of the ZAP scan
                    def scanStatus = "0"
                    while (scanStatus < "100") {
                        sleep 30
                        scanStatus = sh(script: "curl -s '${zapHost}/JSON/ascan/view/status/?apikey=${zapApiKey}'", returnStdout: true).trim()
                        println("ZAP Scan progress: ${scanStatus}%")
                    }

                    // Generate a report of the ZAP scan results
                    sh "curl -s '${zapHost}/OTHER/core/other/htmlreport/?apikey=${zapApiKey}' -o zap-report.html"
                }
            }
        }
    }
}
