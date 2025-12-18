pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    APP_IMAGE = "tg/todo-app"
    DOCKERFILE = "docker/Dockerfile_Mukhamedaliin"
    COMPOSE_FILE = "docker/docker-compose_Mukhamedaliin.yml"
    DOCKERHUB_CREDS = "dockerhub-creds"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git rev-parse --short HEAD || true'
      }
    }

    stage('Build (Gradle)') {
      steps {
        sh '''
          cd app
          chmod +x ./gradlew
          ./gradlew --no-daemon clean bootJar
        '''
      }
    }

    stage('Test') {
      steps {
        sh '''
          cd app
          chmod +x ./gradlew
          ./gradlew --no-daemon test
        '''
      }
      post {
        always {
          archiveArtifacts artifacts: 'app/build/libs/*.jar', fingerprint: true, allowEmptyArchive: true

          junit testResults: 'app/build/test-results/test/*.xml',
          allowEmptyResults: true,
          keepLongStdio: true
        }
      }
    }
    stage('Build Docker Image') {
      steps {
        sh '''
          docker build -f "${DOCKERFILE}" -t "${APP_IMAGE}:${BUILD_NUMBER}" .
          docker tag "${APP_IMAGE}:${BUILD_NUMBER}" "${APP_IMAGE}:latest"
        '''
      }
    }

    stage('Push to DockerHub') {
      steps {
        withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDS}", usernameVariable: 'DH_USER', passwordVariable: 'DH_TOKEN')]) {
          sh '''
            echo "$DH_TOKEN" | docker login -u "$DH_USER" --password-stdin
            docker push "${APP_IMAGE}:${BUILD_NUMBER}"
            docker push "${APP_IMAGE}:latest"
            docker logout
          '''
        }
      }
    }

    stage('Deploy (main only)') {
      when {
        anyOf {
          branch 'main'
          branch 'master'
        }
      }
      steps {
        sh '''
          cd docker
          docker compose -f "${COMPOSE_FILE}" up -d
        '''
      }
    }

    stage('Health Check') {
      steps {
        sh '''
          sleep 10
          curl -f http://localhost:8080/actuator/health
        '''
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'app/build/libs/*.jar', fingerprint: true
      sh 'docker ps || true'
    }
    failure {
      sh 'docker logs todo_app --tail 200 || true'
      sh 'docker logs todo_db --tail 200 || true'
    }
  }
}
