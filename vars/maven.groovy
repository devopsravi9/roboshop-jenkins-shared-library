def call() {
    node() {

        common.pipelineInit()

        stage('build package ') {
            sh '''
                ls -ltr
                mvn clean package
                ls -ltr
                '''
        }
    }
}