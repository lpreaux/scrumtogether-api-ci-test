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
        
        // Configuration du déploiement sur la VM
        DEPLOY_VM_USER = 'deploy'
        DEPLOY_VM_HOST = '192.168.64.4'  // Remplacer par l'IP de votre VM
        DEPLOY_PATH = '/opt/applications'
        DEPLOY_CREDS = 'vm-ssh-key'  // ID des credentials dans Jenkins
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
        
        stage('Unit Tests & Coverage') {
            steps {
                sh 'mvn verify'
                recordCoverage(
                    tools: [[parser: 'JACOCO']],
                    id: 'java-coverage',
                    sourceCodeRetention: 'EVERY_BUILD'
                )
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONAR_SERVER) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${SONAR_PROJECT_KEY} \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.exclusions=**/generated/**,**/model/**,**/config/** \
                        -Dsonar.qualitygate.wait=true
                    """
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
        
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy to VM') {
            steps {
                script {
                    echo "Current branch name: ${env.BRANCH_NAME}"
                    def gitBranch = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                    echo "Current git branch: ${gitBranch}"
                    
                    if (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'master' || gitBranch == 'main') {
                        withCredentials([sshUserPrivateKey(credentialsId: "${DEPLOY_CREDS}", keyFileVariable: 'SSH_KEY')]) {
                            // Création du répertoire de déploiement si nécessaire
                            sh """
                                ssh -i \${SSH_KEY} -o StrictHostKeyChecking=no ${DEPLOY_VM_USER}@${DEPLOY_VM_HOST} '
                                    sudo mkdir -p ${DEPLOY_PATH}
                                    sudo chown ${DEPLOY_VM_USER}:${DEPLOY_VM_USER} ${DEPLOY_PATH}
                                '
                            """
                            
                            // Copie du JAR
                            sh """
                                scp -i \${SSH_KEY} target/*.jar ${DEPLOY_VM_USER}@${DEPLOY_VM_HOST}:${DEPLOY_PATH}/${APP_NAME}.jar
                            """
                            
                            // Copie du fichier service si nécessaire
                            sh """
                                echo '[Unit]
                                Description=ScrumTogether API
                                After=network.target

                                [Service]
                                Type=simple
                                User=${DEPLOY_VM_USER}
                                Environment="SPRING_PROFILES_ACTIVE=prod"
                                WorkingDirectory=${DEPLOY_PATH}
                                ExecStart=/usr/bin/java -jar ${DEPLOY_PATH}/${APP_NAME}.jar
                                SuccessExitStatus=143
                                Restart=always
                                RestartSec=10

                                [Install]
                                WantedBy=multi-user.target' | ssh -i \${SSH_KEY} ${DEPLOY_VM_USER}@${DEPLOY_VM_HOST} 'sudo tee /etc/systemd/system/${APP_NAME}.service'
                            """
                            
                            // Redémarrage du service
                            sh """
                                ssh -i \${SSH_KEY} ${DEPLOY_VM_USER}@${DEPLOY_VM_HOST} '
                                    sudo systemctl daemon-reload
                                    sudo systemctl enable ${APP_NAME}
                                    sudo systemctl restart ${APP_NAME}
                                    sudo systemctl status ${APP_NAME}
                                '
                            """
                        }
                    } else {
                        error "Not on main branch, current branch: ${env.BRANCH_NAME}"
                    }
                }
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
