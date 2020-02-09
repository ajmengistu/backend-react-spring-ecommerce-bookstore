pipeline {
    agent any
    stages {
        stage('test java installation') {
            steps {
                sh 'java -version'
                sh 'which java'
            }
        }
        stage('test maven installation') {
            steps {
                sh 'mvn -version'
                sh 'which mvn'
            }
        }
        
        stage('test docker installation') {
            steps {
                sh 'docker -v'
                sh 'docker version'
                sh 'which docker'
            }
        }
    }
}
