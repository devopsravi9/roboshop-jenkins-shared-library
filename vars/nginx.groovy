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