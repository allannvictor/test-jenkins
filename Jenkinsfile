pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
      		sh "chmod +x gradlew"
            sh "./gradlew clean build"
            }
        }
        stage('Test') {
            agent {
                docker {
                    image 'mysql/mysql-server'
                    args '--name mysql-container -e MYSQL_ROOT_PASSWORD=admin -p 3306:3306 -d'
                }
            }
            steps {
                sh "chmod 755 gradlew"
    		    sh "./gradlew check"
            }
        }
    }
}
