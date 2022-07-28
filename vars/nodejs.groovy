def call() {
    env.EXTRA_OPTS=""
    node() {

        common.pipelineInit()

        stage('download dependencies') {
            sh '''                
                npm install
                '''
        }

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            common.PublishArtiFacts()
        }
    }
}

// publishArtiFact is just a word we using this name for ziping process.
// file should not zip everytime it should happen only when tag creates, i.e, we are releasing new version.