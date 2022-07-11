pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
      		sh "chmod +x gradlew"
            sh "./gradlew bootJar"
            }
        }
        stage('Test') {
            steps {
                sh "chmod 755 gradlew"
    		    sh "./gradlew check"
            }
        }
    }
}
