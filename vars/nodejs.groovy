def call() {
    node() {
        stage('download dependencies') {
            sh '''
                ls -ltr
                npm install
                '''
        }
    }
}