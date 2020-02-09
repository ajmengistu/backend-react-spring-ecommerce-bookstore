pipeline {
    agent any
    tools {
        maven 'maven3.6.3'
    }
    stages {
        stage('test maven installation') {
            steps {
                sh 'mvn -version'
                sh 'which mvn'
            }
        }
    }
}
