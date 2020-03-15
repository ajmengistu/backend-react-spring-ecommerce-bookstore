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
                // Send Slack a notification that the current project has been checked out for building, testing, and deployment.
                // Note: must install the Slack Notification Jenkins plugin to send Slack notifications via slackSend command.
                slackSend(
                    color: "warning", 
                    message: "Started `${env.JOB_NAME}#${env.BUILD_NUMBER}`"
                )
            }
        }
        stage('Build') {
            steps {
                echo '-- Building project --'
                 // build project, but skip running tests
                sh 'mvn clean install -DskipTests=true'
                slackSend(
                    color: "good", 
                    message: "Build successful: `${env.JOB_NAME}#${env.BUILD_NUMBER}` <${env.BUILD_URL}|Open in Jenkins>"
                )
            }
        }
        stage('Test') {
            steps {
               echo '-- Testing project --'        
               sh 'mvn test -Dspring.profiles.active=test'
               slackSend(
                   color: "good", 
                   message: "Tests successful: `${env.JOB_NAME}#${env.BUILD_NUMBER}` <${env.BUILD_URL}|Open in Jenkins>"
               )
            }
        }
        // stage("build & SonarQube analysis") {
        //     steps {
        //       // withSonarQubeEnv('SonarQubeScanner') {
        //         // sh 'mvn clean package sonar:sonar -Dspring.profiles.active=test -Dsonar.host.url=http://localhost:9000 -DskipTests=true'
        //         sh 'mvn clean package sonar:sonar -Dspring.profiles.active=test -Dsonar.host.url=http://8095fdca.ngrok.io -DskipTests=true'
        //       // }
        //     }
        // }
        // stage("Quality Gate") {
        //     steps {
        //       timeout(time: 1, unit: 'MINUTES') {
        //         waitForQualityGate abortPipeline: true
        //       }
        //     }
        // }
        stage('Deploy to Heroku') {
            steps {
                echo '-- Deploying project to Heroku --'
                // The prod application is specified in the heroku plugin in pom.xml.
                // The following command heroku:deploy is referencing the heroku plugin in pom.xml.
                sh 'mvn clean heroku:deploy -Dspring.profiles.active=test'
                slackSend(
                    color: "good", 
                    message: "Deployment to Heroku successful: `${env.JOB_NAME}#${env.BUILD_NUMBER}` <${env.BUILD_URL}|Open in Jenkins>"
                )
            }
        }
    }
}
