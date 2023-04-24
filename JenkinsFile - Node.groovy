pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'http://IP-Host:9000'
        PROJECT_NAME = 'Unimed'
        PROJECT_VERSION = '1.0.0'
    }

    tools {
        nodejs 'Nodejs'
    }

    stages {
        stage('Checkout') {
            steps {
                // Faça o checkout do código do repositório
                git 'https://github.com/Cloud3-SA/devsecops.git'
            }
        }
        
        stage('Build') {
            steps {
                script {
                    def nodeHome = tool 'Nodejs'
                    env.PATH = "${nodeHome}/bin:${env.PATH}"
                }
                sh 'npm ci'
            }
        }


        stage('SonarQube Analysis') {
            steps {
                script {
                    // Analise o código com o SonarQube
                    withSonarQubeEnv('Sonarqube') {
                        withCredentials([string(credentialsId: 'Sonar', variable: 'SONAR_TOKEN')]) {
                            sh "npm run sonar -- -Dsonar.projectKey='Unimed' -Dsonar.projectName='Unimed' -Dsonar.login=$SONAR_TOKEN"
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 1, unit: 'HOURS') {
                        // Use 'credentialsId' instead of passing the token directly
                        waitForQualityGate credentialsId: 'Sonar'
                    }
                }
            }
        }

    }
}
