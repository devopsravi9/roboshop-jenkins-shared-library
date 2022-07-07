// here we removing files in workstation from /ome/centos/workspace/ci-pipeline_cart_main

def pipelineInit() {
        stage('clear old files & cloneing git again') {
            sh 'rm -rf *'
            git branch: 'main', url: "https://github.com/devopsravi9/${COMPONENT}.git"
        }
}
// consider list from ls -ltr and zip the required files only, then move it nexus repo node_modules was the folder that comes after npm insatll

def PublishArtiFacts() {
    stage('prepare ArtiFacts') {
        if ( env.APP_TYPE == nodejs ) {
            sh '''
                zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
            '''
        }
    }
}