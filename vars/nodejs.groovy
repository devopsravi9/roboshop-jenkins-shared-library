def call() {
    env.EXTRA_OPTS=""
    node() {

        common.pipelineInit()
        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            sh 'git checkout ${TAG_NAME}'
        }

        //sh 'env'

        stage('download dependencies') {
            sh '''                
                npm install
                '''
        }

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            //common.PublishArtiFacts()
            common.PublishlocalArtiFacts()
            //this is added for immutable approach
            common.PublishAMI ()
        }
    }
}

// publishArtiFact is just a word we using this name for ziping process.
// file should not zip everytime it should happen only when tag creates, i.e, we are releasing new version.