pipeline {
    agent any

    environment {
        // Force Java 21
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"

        // Docker image name
        IMAGE_NAME = "my-vaja-project"

        // Docker tag = Jenkins build number (NO CONFUSION)
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
                echo "📦 Verifying JAR artifact..."
                sh 'ls -lh target'
            }
        }

        stage('Docker Build') {
            steps {
                echo "🐳 Building Docker image with tag ${IMAGE_TAG}..."
                sh '''
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                '''
            }
        }

        stage('Docker Run') {
            steps {
                echo "▶️ Running Docker container..."

                sh '''
                    # Stop and remove old container if exists
                    docker rm -f ${IMAGE_NAME} || true

                    # Run container using BUILD_NUMBER tag
                    docker run -d --name ${IMAGE_NAME} ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Docker Cleanup (Keep last 2 images)') {
    steps {
        echo "🧹 Cleaning old Docker images (keeping latest 2)..."
        sh '''
            # Get all tags for my-vaja-project, sort numerically, skip last 2
            OLD_IMAGES=$(docker images my-vaja-project --format "{{.Tag}}" \
              | grep -E '^[0-9]+$' \
              | sort -n \
              | head -n -3)

            if [ -n "$OLD_IMAGES" ]; then
              for TAG in $OLD_IMAGES; do
                echo "Deleting my-vaja-project:$TAG"
                docker rmi my-vaja-project:$TAG || true
              done
            else
              echo "No old images to delete"
            fi
        '''
    }
}


        stage('Show Application Output') {
            steps {
                echo "📜 Application output:"
                sh '''
                    sleep 3
                    docker logs ${IMAGE_NAME}
                '''
            }
        }
    }

    post {
        success {
            echo "✅ PIPELINE SUCCESSFUL – Docker image tagged with build number: ${BUILD_NUMBER}"
        }
        failure {
            echo "❌ PIPELINE FAILED – check logs above"
        }
        // Workspace intentionally NOT cleaned
    }
}
