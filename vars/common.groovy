def pipelineInit() {
        stage('clear old files & cloneing git again') {
            sh 'rm -rf *'
            git branch: 'main', url: 'https://github.com/devopsravi9/cart.git'

        }
    }
}