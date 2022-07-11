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
        agent {
            docker {
                image 'mysql/mysql-server'
                 args '--name mysql-container -e MYSQL_ROOT_PASSWORD=admin -p 3306:3306 -d mysql'}
            }
            steps {
                sh "chmod 755 gradlew"
    		    sh "./gradlew check"
            }
        }
    }
}
