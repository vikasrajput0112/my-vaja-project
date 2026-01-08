pipeline {
    agent any

    environment {
        APP_NAME = "my-vaja-project"
        APP_PORT = "8081"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📥 Checking out source code..."
                checkout scm
            }
        }

        stage('Verify Java') {
            steps {
                sh '''
                    java -version
                    javac -version
                '''
            }
        }

        stage('Build with Maven') {
            steps {
                echo "🔨 Building Spring Boot application..."
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                echo "🐳 Building Docker image..."
                sh '''
                    docker build -t ${APP_NAME}:${IMAGE_TAG} .
                '''
            }
        }

        stage('Docker Run') {
            steps {
                echo "🚀 Deploying container on port ${APP_PORT}..."

                sh '''
                    docker stop ${APP_NAME} || true
                    docker rm ${APP_NAME} || true

                    docker run -d \
                      -p ${APP_PORT}:${APP_PORT} \
                      --name ${APP_NAME} \
                      ${APP_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Docker Cleanup (Keep last 2 + prune)') {
            steps {
                sh '''
                    OLD_IMAGES=$(docker images ${APP_NAME} --format "{{.Tag}}" \
                      | grep -E '^[0-9]+$' \
                      | sort -n \
                      | head -n -2)

                    for TAG in $OLD_IMAGES; do
                        docker rmi ${APP_NAME}:$TAG || true
                    done

                    docker image prune -f
                '''
            }
        }

        stage('Show Application URL') {
            steps {
                echo "✅ Application deployed successfully!"
                echo "🌐 URL: http://<SERVER-IP>:${APP_PORT}/"
            }
        }
    }

    post {
        success {
            echo "🎉 PIPELINE SUCCESS"
        }
        failure {
            echo "❌ PIPELINE FAILED"
        }
    }
}
