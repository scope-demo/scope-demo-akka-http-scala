pipeline {
    agent any

    stages {
       stage('Build') {
            parallel {
                stage('JDK11') {
                    steps {
                        sh 'echo build'
                        sh 'COMMIT=${GIT_COMMIT} docker-compose -p 11-jdk-${GIT_COMMIT} build --build-arg JDK="11-jdk"'
                    }
                }
            }
        }

        stage('Test'){
            parallel {
                stage('JDK11') {
                    steps {
                        sh 'echo test'
                        sh 'IS_CI=true COMMIT=${GIT_COMMIT} docker-compose -p 11-jdk-${GIT_COMMIT} up --exit-code-from=scope-demo-akka-http-scala scope-demo-akka-http-scala'
                    }
                }
            }
        }
    }

    post {
        always {
            sh 'COMMIT=${GIT_COMMIT} docker-compose -p 11-jdk-${GIT_COMMIT} down -v'
        }
    }

}