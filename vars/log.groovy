def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}

// this is declarative approch. but we are going scripted approach for shared library. lets see

//
//def call(InDirectVar) {
//    pipeline {
//        agent any
//        stages {
//            stage('one') {
//                steps {
//                    sh "echo one- ${InDirectVar}"
//                }
//            }
//            stage('two') {
//                steps {
//                    sh 'echo two- ${DirectVar}'
//                }
//            }
//        }
//    }
//}

// SCRIPTED approach. IN scripted approach we can also write code in between them.

def call() {
    node () {
        stage ('one') {
            sh "echo one"
        }
        stage('two') {
            sh "two - ${DirectVar}"
        }
    }
}