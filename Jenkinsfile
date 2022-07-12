pipeline {
    //agent any
    agent { docker { image 'openjdk:17' } }
    stages {
    stage('docker permission'){
        steps{
            sh "sudo usermod -aG docker jenkins"
        }
    }
//         stage('Build') {
//             steps {
//       		sh "chmod +x gradlew"
//             sh "./gradlew clean build"
//             }
//         }
        stage('Test') {
//             agent {
//                 docker {
//
//                     image 'openjdk:17'
//                     //args '--name mysql-container -e MYSQL_ROOT_PASSWORD=admin -p 3306:3306 -d'
//                 }
//             }
            steps {
                sh "chmod 755 gradlew"
    		    sh "./gradlew check"
            }
        }
    }
}
