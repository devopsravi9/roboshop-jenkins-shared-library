// here we removing files in workstation from /ome/centos/workspace/ci-pipeline_cart_main

def pipelineInit() {
        stage('clear old files & cloneing git again') {
            sh 'rm -rf *'
            git branch: 'main', url: "https://github.com/devopsravi9/${COMPONENT}.git"
        }
}
// consider list from ls -ltr and zip the required files only, then move it nexus repo node_modules was the folder that comes after npm insatll


def PublishArtiFacts() {
    stage("Prepare Artifacts") {
        sh "echo ${APP_TYPE}"

        if (env.APP_TYPE == "nodejs") {
            sh """
                zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                """
        }
        if (env.APP_TYPE == "maven") {
            sh 'ls -l'
            sh """
                cp target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
                zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
                """
        }
        if (env.APP_TYPE == "python") {
            sh """
                zip -r ${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
                """
        }
        if (env.APP_TYPE == "nginx") {
            sh """
                cd static
                zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                """
        }
    }
    stage('upload artifact to nexus') {
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'pass', usernameVariable: 'user')]) {
            sh """
                curl -v -u ${user}:${pass} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.2.48:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                """
            }
        }
    }