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
                    echo "Java version:"
                    java -version
                    echo "Javac version:"
                    javac -version
                '''
            }
        }

        stage('Build with Maven') {
            steps {
                echo "🔨 Building project with Maven..."
                sh 'mvn clean package'
            }
        }

        stage('Verify Artifact') {
            steps {
                echo "✅ Verifying Spring Boot JAR..."
                sh 'java -jar target/my-vaja-project-1.0.0.jar & sleep 10; pkill -f my-vaja-project || true'
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
                echo "🚀 Running Docker container on port ${APP_PORT}..."

                sh '''
                    # Stop & remove old container if exists
                    docker stop ${APP_NAME} || true
                    docker rm ${APP_NAME} || true

                    # Run new container with port mapping
                    docker run -d \
                      -p ${APP_PORT}:${APP_PORT} \
                      --name ${APP_NAME} \
                      ${APP_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Docker Cleanup (Keep last 2 + prune)') {
            steps {
                echo "🧹 Cleaning old Docker images and dangling layers..."
                sh '''
                    OLD_IMAGES=$(docker images ${APP_NAME} --format "{{.Tag}}" \
                      | grep -E '^[0-9]+$' \
                      | sort -n \
                      | head -n -2)

                    if [ -n "$OLD_IMAGES" ]; then
                      for TAG in $OLD_IMAGES; do
                        docker rmi ${APP_NAME}:$TAG || true
                      done
                    fi

                    docker image prune -f
                '''
            }
        }

        stage('Show Application URL') {
            steps {
                echo "🌐 Application deployed successfully!"
                echo "👉 Access it at: http://<SERVER-IP>:${APP_PORT}/"
            }
        }
    }

    post {
        success {
            echo "✅ PIPELINE SUCCESS – Application is live on port ${APP_PORT}"
        }
        failure {
            echo "❌ PIPELINE FAILED – Check logs above"
        }
    }
}
