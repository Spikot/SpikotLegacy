pipeline{
    agent any

    environment{
        MAVEN_CREDENTIAL = credentials('heartpattern-maven-repository')
    }

    stages{
        stage('daemon'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true --daemon'
            }
        }
        stage('clean'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true clean'
            }
        }
        stage('compile'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true classes testClasses'
            }
        }
        stage('test'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true test'
            }
        }
        stage('create plugin'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true createPlugin'
            }
        }
        stage('publish'){
            steps{
                sh './gradlew -PnexusUser=${MAVEN_CREDENTIAL_USR} -PnexusPassword=${MAVEN_CREDENTIAL_PSW} -Ddeployment=true publish'
            }
        }
    }
    post{
        always{
            archiveArtifacts artifacts: 'build/libs/*', fingerprint: true
        }
    }
}