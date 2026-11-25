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
            choices: ['full-e2e', 'parallel-grid', 'smoke', 'sanity', 'regression'],
            description: 'Maven TestNG profile to execute'
        )
        booleanParam(
            name: 'USE_GRID',
            defaultValue: false,
            description: 'Spin up Docker Selenium Grid containers and execute remotely (-Dgrid_run=Y)'
        )
        string(
            name: 'GRID_TARGET_URL',
            defaultValue: 'http://172.24.240.1:8080/',
            description: 'Jenkins URL reachable from inside Docker browser containers'
        )
    }

    environment {
        PATH          = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        JENKINS_TOKEN = credentials('jenkins-api-token')
        JENKINS_PASS  = credentials('jenkins-ui-pass')
        GRID_FLAG     = "${params.USE_GRID ? 'Y' : 'N'}"
        HEADLESS_FLAG = "${params.HEADLESS ? 'Y' : 'N'}"
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

        stage('Start Docker Selenium Grid') {
            when {
                expression { return params.USE_GRID }
            }
            steps {
                echo 'Spinning up Docker Selenium Grid (Hub + Chrome & Edge nodes)...'
                bat 'docker compose up -d'
                bat 'ping -n 10 127.0.0.1 > nul'
            }
        }

        stage('Compile & Static Check') {
            steps {
                bat 'mvn clean test-compile'
            }
        }

        stage('Execute Automated Test Suite') {
            steps {
                script {
                    def urlOverride = params.USE_GRID ? "-Durl=${params.GRID_TARGET_URL}" : ''
                    bat 'mvn test -P' + params.SUITE_PROFILE + ' -Denv=' + params.TARGET_ENV + ' -Dheadless=Y -Dgrid_run=' + env.GRID_FLAG + ' ' + urlOverride + ' -DargLine="-Xmx512m"'
                }
            }
        }
    }

    post {
        always {
            script {
                if (params.USE_GRID) {
                    echo 'Tearing down Docker Selenium Grid containers...'
                    bat 'docker compose down'
                }
            }
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'reports/**/*.html, reports/**/*.png', allowEmptyArchive: true
        }
        success {
            echo "Pipeline PASSED for environment: ${params.TARGET_ENV} (Grid=${env.GRID_FLAG})"
        }
        failure {
            echo "Pipeline FAILED — inspect Surefire XML results and archived ExtentReports/screenshots."
        }
    }
}
