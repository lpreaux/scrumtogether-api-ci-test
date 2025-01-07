pipeline {
    agent any
    
    tools {
        maven 'maven-3.9.9'
    }
    
    environment {
        SONAR_PROJECT_KEY = 'scrumtogether-project'
        SONAR_SERVER = 'SonarQube'
        DOCKER_IMAGE = 'scrumtogether-api'
        VERSION = '1.0.0'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Code Coverage') {
            steps {
                jacoco(
                    execPattern: '**/target/jacoco.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java',
                    exclusionPattern: '**/src/test*'
                )
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONAR_SERVER) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.exclusions=**/generated/**,**/model/**,**/config/**
                    """
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            when {
                branch 'main'
            }
            steps {
                sh """
                    docker build -t ${DOCKER_IMAGE}:${VERSION} .
                    docker tag ${DOCKER_IMAGE}:${VERSION} ${DOCKER_IMAGE}:latest
                """
            }
        }
        
        stage('Deploy to Staging') {
            when {
                branch 'main'
            }
            steps {
                sh """
                    docker stop ${DOCKER_IMAGE}-staging || true
                    docker rm ${DOCKER_IMAGE}-staging || true
                    docker run -d --name ${DOCKER_IMAGE}-staging -p 8080:8080 ${DOCKER_IMAGE}:${VERSION}
                """
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
