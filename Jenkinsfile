pipeline {
    agent any
    environment {
        DOCKER_CRED = credentials("DOCKER_CRED_ID")
        IMAGE_NAME = "$DOCKER_CRED_USR/daybook3-build-$BUILD_NUMBER"
    }
    stages {
        stage('git branch main') {
            steps {
                git branch: 'main', url: 'https://github.com/vskurikhin/DayBook-3.0.git'
            }
        }
        stage('gradle clean build') {
            steps {
                sh '''
                  set +x
                  gradle clean build \
                        -Dquarkus.package.type=native \
                        -Dquarkus.native.remote-container-build=true
                '''
            }
        }
        stage('docker build') {
            steps {
                echo "for: ${IMAGE_NAME}"
                sh '''
                  set +x
                  docker build \
                      -f src/main/docker/Dockerfile.native \
                      -t ${IMAGE_NAME} \
                      .
                '''
            }
        }
        stage('docker login') {
            steps {
                sh '''
                  set +x
                  docker login --username ${DOCKER_CRED_USR} --password ${DOCKER_CRED_PSW}
                '''
            }
        }
        stage('docker push') {
            steps {
                echo "for: ${IMAGE_NAME}"
                sh '''
                  set +x
                  docker push ${IMAGE_NAME}
                '''
            }
        }
    }
}