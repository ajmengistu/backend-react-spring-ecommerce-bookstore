pipeline {
    agent any
    stages {
        stage('Back-end') {            
            steps {
                echo '-- Checking out project repository --'
                checkout scm 
                // sh 'git log --pretty="[%h] %an: %s" -1 HEAD > LAST_GIT_COMMIT'
                // def lastGitCommit = readFile('LAST_GIT_COMMIT')
                // echo '${lastGitCommit}'
            }
            steps {
                echo '-- Build, Test, & Package Spring server repository --'
                sh './mvnw clean package'
            }
        }
    }
}
