def call() {
    env.EXTRA_OPTS=""
    node () {

        common.pipelineInit()
        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            sh 'git checkout ${TAG_NAME}'
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

// in python script we download dependencies on server side not on ci side
//so we zip all the files. in those files requirements.txt contains the list of dependencies to be
//downloaded on server side.