def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}

def call(InDirectVar) {
    pipeline {
        agent any
        stages {
            stage('one') {
                steps {
                    sh "echo one- ${InDirectVar}"
                }
            }
            stage('two') {
                steps {
                    sh 'echo two- ${DirectVar}'
                }
            }
        }
    }
}