def call() {
    node() {

        common.pipelineInit()

        stage('download dependencies') {
            sh '''
                ls -ltr
                npm install
                '''
        }
    }
}