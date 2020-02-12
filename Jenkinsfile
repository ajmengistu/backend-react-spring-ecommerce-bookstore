pipeline {
    agent any
    tools {
        maven 'maven3.6.3'
    }
    stages {
        // stage('test java installation') {
        //     steps {
        //         sh 'java -version'
        //         sh 'which java'
        //     }
        // }
        // stage('test maven installation') {
        //     steps {
        //         sh 'mvn -version'
        //         sh 'which mvn'
        //     }
        // }
        stage('Checkout spring java project') {
            steps {
                echo '-- Checking out project repository --'
                // checkout code from repo
                checkout scm
            }
        }
        // stage('Build') {
        //     steps {
        //         echo '-- Building project --'
        //         // build project, but skip running tests
        //         sh 'mvn clean install -DskipTests=true'
        //     }
        // }
        // stage('Test') {
        //     steps {
        //         echo '-- Testing project --'        
        //         sh 'mvn test -Dspring.profiles.active=test'
        //     }
        // }
        stage("build & SonarQube analysis") {
            steps {
              // withSonarQubeEnv('SonarQubeScanner') {
                sh 'mvn clean package sonar:sonar -Dspring.profiles.active=test -Dsonar.host.url=http://localhost:9000 -DskipTests=true'
              // }
            }
        }
        stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
              }
            }
        }
        // stage('Deploy to Heroku') {
        //     steps {
        //         echo '-- Deploying project to Heroku --'
        //         sh 'mvn clean heroku:deploy -Dspring.profiles.active=test'
        //     }
        // }
    }
}
