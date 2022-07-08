def call() {
    node() {

        common.pipelineInit()

        stage('build package ') {
            sh '''
                mvn clean package
            '''
        }

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            common.PublishArtiFacts()
        }

    }
}

