pipeline {
    agent any

    environment {
        // Explicitly pin Java 21 (VERY IMPORTANT)
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Java Version') {
            steps {
                sh '''
                    echo "Java version:"
                    java -version
                    echo "Javac version:"
                    javac -version
                '''
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build SUCCESSFUL – my-vaja-project'
        }
        failure {
            echo '❌ Build FAILED – check logs'
        }
        always {
            cleanWs()
        }
    }
}
