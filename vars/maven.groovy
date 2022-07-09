def call() {
    env.EXTRA_OPTS='-Dsonar.java.binaries=./target'
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

