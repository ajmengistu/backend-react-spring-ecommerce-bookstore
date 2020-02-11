pipeline {
    agent any
    tools {
        maven 'maven3.6.3'
    }
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
        stage('Checkout spring java project') {
            steps {
                echo '-- Checking out project repository --'
                // checkout code from repo
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo '-- Building project --'
                // build project, but skip running tests
                sh 'mvn clean install -DskipTests=true'
            }
        }
        stage('Test') {
            steps {
                echo '-- Testing project --'        
                sh 'mvn test -Dspring.profiles.active=test'
            }
        }
    }
}
