pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
      		sh "chmod +x gradlew"
            sh "./gradlew clean"
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
