pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    environment{
        SCANNER_HOME = tool 'sonar-scanner'
    }

    stages {
        stage('Git Checkout') {
            steps {
                git changelog: false, credentialsId: 'Github cred', poll: false, url: 'https://github.com/mayurchakalasiya1990/spring-security-jwt'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn clean compile -DskipTests=true'
            }
        }
        stage('OWASP Scan') {
            steps {
                dependencyCheck additionalArguments: ' --scan ./ ', odcInstallation: 'DP-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.html'
            }
        }
        stage('Sonarqube Scan') {
            steps {
                withSonarQubeEnv('sonar-server'){
                    sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName=jwt-security-demo \
                    -Dsonar.java.binaries=. \
                    -Dsonar.projectKey=jwt-security-demo '''
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Docker Build & Push') {
            steps {
                // This step should not normally be used in your script. Consult the inline help for details.
                script{
                    withDockerRegistry(credentialsId: '22a7b55e-6958-400b-9720-92abff5700e0', toolName: 'docker') {
                        sh 'docker build -t spring-security-jwt -f Dockerfile .'
                        sh 'docker tag spring-security-jwt mayurchakalasiya1410/spring-security-jwt:latest'
                        sh 'docker push mayurchakalasiya1410/spring-security-jwt:latest'
                    }
                }

            }

        }
        stage('Docker Deploy') {
            steps {
                // This step should not normally be used in your script. Consult the inline help for details.
                script{
                    withDockerRegistry(credentialsId: '22a7b55e-6958-400b-9720-92abff5700e0', toolName: 'docker') {
                        sh "docker run -d -p 8070:8070 mayurchakalasiya1410/spring-security-jwt:latest"
                    }
                }

            }

        }
    }
}
