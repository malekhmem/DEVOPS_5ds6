pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "salma597"
    }

    stages {
        stage('Get Started') {
            steps {
                echo "Start Building Pipeline"
            }
        }

        stage("Clone from Git") {
            steps {
                git url: 'https://github.com/malekhmem/DEVOPS_5ds6.git',
                    branch: 'salmaridene'
            }
        }

        stage('Clean') {
            steps {
                echo 'Cleaning previous builds and cache...'
                sh 'mvn clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building the Spring Boot application...'
                sh 'mvn package'
            }
        }

        stage('Static Analysis') {
            environment {
                scannerHome = tool 'sonnarqubeScanner'
            }
            steps {
                withCredentials([string(credentialsId: 'sonartoken', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('Sonarqube') {
                        sh "${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=projetski \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.sources=src/main/java \
                            -Dsonar.host.url=http://192.168.1.7:9002 \
                            -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }
    }
}
