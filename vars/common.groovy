// here we removing files in workstation from /ome/centos/workspace/ci-pipeline_cart_main

def pipelineInit() {
        stage('clear old files & cloneing git again') {
            //sh 'rm -rf *'
            sh 'find . | sed -e "1d" | xargs rm -rf '
            git branch: 'main', url: "https://github.com/devopsravi9/${COMPONENT}.git"
        }
}
// consider list from ls -ltr and zip the required files only, then move it nexus repo node_modules was the folder that comes after npm install


def PublishArtiFacts() {
    env.ENV="dev"
    stage("Prepare Artifacts") {

        if (env.APP_TYPE == "nodejs") {
            sh """
                zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                """
        }
        if (env.APP_TYPE == "maven") {
            sh """
                cp target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
                zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
                """
        }
        if (env.APP_TYPE == "python") {
            sh """
                zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
                """
        }
        if (env.APP_TYPE == "nginx") {
            sh """
                cd static
                zip -r ../${ENV}-${COMPONENT}-${TAG_NAME}.zip *
                """
        }
    }
    stage('upload artifact to nexus') {
        // this is method of accesing credentials in scripted pipeline. also this can genrate from pipeline syntax script generator by using with credentials option. https://docs.cloudbees.com/docs/cloudbees-ci/latest/cloud-secure-guide/injecting-secrets
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'pass', usernameVariable: 'user')]) {
            sh """
                curl -v -u ${user}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://172.31.2.48:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
                """
            }
        }

    stage('deploy to dev env') {
        build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "${ENV}"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
        }

    stage('run smoke test') {
        sh "echo run smoke test"
        }

    promoterelease("dev","qa")

    stage('deploy to qa env') {
        // build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "qa"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
        sh "echo deploy to qa environment"
    }

    testRuns ()

    stage('run smoke test') {
        sh "echo run smoke test"
    }

    promoterelease("qa","prod")

    }

def promoterelease(SOURCE_ENV,ENV) {
    stage("upload artifact to ${ENV} environment")
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'pass', usernameVariable: 'user')]) {
            sh """
                cp ${SOURCE_ENV}-${COMPONENT}-${TAG_NAME}.zip ${ENV}-${COMPONENT}-${TAG_NAME}.zip
                curl -v -u ${user}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://172.31.2.48:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
             """
    }
}

def CodeChecks () {
    stage('code quality checks & unit test') {
        parallel([
            CodeCheck: {
                withCredentials([usernamePassword(credentialsId: 'SONARCUBE', passwordVariable: 'pass', usernameVariable: 'user')]) {
                    //sh "sonar-scanner -Dsonar.projectKey=${COMPONENT}  -Dsonar.host.url=http://172.31.4.137:9000 -Dsonar.login=${user} -Dsonar.password=${pass}  ${EXTRA_OPTS}"
                    //sh "sonar-quality-gate.sh ${user} ${pass} 172.31.4.137 ${COMPONENT}"
                    // can check above command in ct /usr/bin/sonar-quality-gate.sh  a prewritten script.
                    sh "echo code analysis"
                }
            },
            UnitTest: {
                UnitTest()
            }
        ])
    }
}

def UnitTest() {
    if (env.APP_TYPE == "nodejs") {
        sh """
            # npm run test
            echo run test cases
        """
    }
    if (env.APP_TYPE == "maven") {
        sh """
            # mvn test
            echo run test cases
            """
    }
    if (env.APP_TYPE == "python") {
        sh """
            # python -m unit test
            echo run test cases
            """
    }
    if (env.APP_TYPE == "nginx") {
        sh """
            # npm run test
            echo run test cases
            """
    }
}

def testRuns () {
    stage('integration, pentest & e2etest') {
        parallel([
            integrationtest : {
                sh "echo integration test"
                },
            PenTest: {
                sh "echo integration test"
                },
            e2eTest: {
                sh "echo end 2 end test"
            }
        ])
    }
}


def PublishAMI () {
    ansiColor('xterm') {
        stage ('Publish AMI') {
            sh '''
                terraform init
                terraform apply -auto-approve -var APP_VERSION=${TAG_NAME}
                # this to remove state file of  ami so it cant destroy ami image  
                terraform state rm module.ami.aws_ami_from_instance.ami
                terraform destroy -auto-approve -var APP_VERSION=${TAG_NAME}
            '''
        }
    }
}

def PublishlocalArtiFacts () {
    env.ENV="dev"
    stage("Publish local Artifacts") {

        if (env.APP_TYPE == "nodejs") {
            sh """
                zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                """
        }
        if (env.APP_TYPE == "maven") {
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
}


//in nginx ../ is given to create zip before static dir, i.e, in parent dir of static dir

