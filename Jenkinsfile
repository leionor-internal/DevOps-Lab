pipeline {
    agent any

    tools {
        maven 'Maven3'   // Name must match a Maven installation configured in
                         // Manage Jenkins > Tools
    }

    environment {
        IMAGE_NAME = "devops-cicd-lab"
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        CONTAINER_NAME = "devops-cicd-lab-container"
        DOCKERHUB_CREDS = credentials('dockerhub-creds') // configured in Jenkins credentials store
        DOCKERHUB_REPO  = "likithus/ics-build-demo:tagname"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/leionor-internal/DevOps-Lab.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package JAR') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    sh "echo ${DOCKERHUB_CREDS_PSW} | docker login -u ${DOCKERHUB_CREDS_USR} --password-stdin"
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_REPO}:${IMAGE_TAG}"
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_REPO}:latest"
                    sh "docker push ${DOCKERHUB_REPO}:${IMAGE_TAG}"
                    sh "docker push ${DOCKERHUB_REPO}:latest"
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sh """
                    docker rm -f ${CONTAINER_NAME} || true
                    docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully. Container is running.'
        }
        failure {
            echo 'Pipeline failed. Check the stage logs above.'
        }
        always {
            sh 'docker logout || true'
        }
    }
}
