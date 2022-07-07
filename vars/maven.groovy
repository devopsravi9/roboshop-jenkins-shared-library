def call() {
    node() {

        common.pipelineInit()

        stage('build package ') {
            sh '''
                ls -ltr
                maven clean package
                '''
        }
    }
}