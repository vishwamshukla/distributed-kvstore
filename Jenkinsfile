pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'your-dockerhub-username/distributed-kvstore:latest'
        K8S_NAMESPACE = 'default'
    }

    tools {
        maven 'Maven'  // Use the name defined in Jenkins Maven configuration
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo 'Building JAR file...'
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    docker push ${DOCKER_IMAGE}
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'
                sh '''
                kubectl apply -f k8s/redis-deployment.yaml
                kubectl apply -f k8s/redis-service.yaml
                kubectl apply -f k8s/app-deployment.yaml
                kubectl apply -f k8s/app-service.yaml
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying deployment...'
                sh 'kubectl get pods'
                sh 'kubectl get services'
            }
        }
    }

    post {
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed. Check the logs for errors.'
        }
    }
}