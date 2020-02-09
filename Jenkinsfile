// source: https://jenkins.io/doc/pipeline/examples/
node {
    // stage('Step up environment with specific versions of MAVEN & JDK') {
        // echo '-- Setting up environment --'
        // Advice: don't define M2_HOME in general. Maven will autodetect its root fine.
       // withEnv(["JAVA_HOME=${ tool 'jdk-1.8.0_64bits' }", "PATH+MAVEN=${tool 'maven-3.2.1'}/bin:${env.JAVA_HOME}/bin"]) {

            // Apache Maven related side notes:
            // --batch-mode : recommended in CI to inform maven to not run in interactive mode (less logs)
            // -V : strongly recommended in CI, will display the JDK and Maven versions in use.
            //      Very useful to be quickly sure the selected versions were the ones you think.
            // -U : force maven to update snapshots each time (default : once an hour, makes no sense in CI).
            // -Dsurefire.useFile=false : useful in CI. Displays test errors in the logs directly (instead of
            //                            having to crawl the workspace files to see the cause).
         //   sh "mvn --batch-mode -V -U -e clean deploy -Dsurefire.useFile=false"
     //   }
    // }
    
    stage('Checkout') {
        echo '-- Checking out project repository --'
        // checkout code from repo
        checkout scm
    }
    
    stage('Build') {
        echo '-- Building project --'
        // build project, but skip running tests
        sh 'mvn --version'
        sh 'java --version'
        sh 'mvn clean install -DskipTests'
    }
    
    stage('Test') {
        echo '-- Testing project --'        
        try {
            // run tests after project build
            sh 'mvn test'
        } catch(e) {
            // if any exception occurs, mark the build as failed
            currentBuild.result = 'FAILURE'
            throw e
        }
    }
}
