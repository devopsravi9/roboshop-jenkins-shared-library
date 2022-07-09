def call() {
    node () {

        common.pipelineInit()

        common.CodeChecks()

        if ( env.BRANCH_NAME == env.TAG_NAME ) {
            common.PublishArtiFacts()
        }
    }
}