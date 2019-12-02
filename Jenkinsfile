node{
    agent any

    environment{
        MAVEN_CREDENTIAL = credentials('heartpattern-maven-repository')
    }

    stages{
        stage('clone'){
            checkout scm
        }
        stage('test'){
            steps{
                sh './gradlew clean test'
            }
        }
        stage('create plugin'){
            steps{
                sh './gradlew createPlugin'
            }
        }
        stage('publish'){
            steps{
                sh './gradlew publish -PnexusUser=$MAVEN_CREDENTIAL_USR -PnexusPassword=$MAVEN_CREDENTIAL_PSW'
            }
        }
    }
}