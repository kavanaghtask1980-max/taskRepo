pipeline {
    agent any

    parameters {
        choice(
            name: 'TARGET_ENV',
            choices: ['qa', 'staging', 'prod'],
            description: 'Target environment passed to ConfigManager (-Denv)'
        )
        choice(
            name: 'SUITE_PROFILE',
            choices: ['full-e2e', 'smoke', 'sanity', 'regression'],
            description: 'Maven TestNG profile to execute'
        )
        booleanParam(
            name: 'USE_GRID',
            defaultValue: false,
            description: 'Execute browsers against Selenium Grid (-Dgrid_run=Y)'
        )
    }

    environment {
        JENKINS_TOKEN = credentials('jenkins-api-token')
        JENKINS_PASS  = credentials('jenkins-ui-pass')
        GRID_FLAG     = "${params.USE_GRID ? 'Y' : 'N'}"
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout & Validate Environment') {
            steps {
                echo "Running [${params.SUITE_PROFILE}] against [${params.TARGET_ENV}] | Grid=${env.GRID_FLAG}"
                bat 'mvn -version'
            }
        }

        stage('Compile & Static Check') {
            steps {
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute Automated Test Suite') {
            steps {
                bat "mvn test -P${params.SUITE_PROFILE} -Denv=${params.TARGET_ENV} -Dheadless=Y -Dgrid_run=${env.GRID_FLAG}"
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'reports/**/*.html, reports/**/*.png', allowEmptyArchive: true
        }
        success {
            echo "Pipeline PASSED for environment: ${params.TARGET_ENV}"
        }
        failure {
            echo "Pipeline FAILED — inspect Surefire XML results and archived ExtentReports/screenshots."
        }
    }
}
