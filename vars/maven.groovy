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

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            common.PublishArtiFacts()
        }

    }
}

