def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}

def call() {
    pipeline {
        agent any
        satges {
            stage('one') {
                steps {
                    sh 'echo one'
                }
            }
            stage('two') {
                steps {
                    sh 'echo two'
                }
            }
        }
    }
}