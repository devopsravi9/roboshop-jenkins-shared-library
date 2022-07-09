def call() {
    node () {

        common.pipelineInit()

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            common.PublishArtiFacts()
        }
    }
}

// in python script we download dependencies on server side not on ci side
//so we zip all the files. in those files requirements.txt contains the list of dependencies to be
//downloaded on server side.