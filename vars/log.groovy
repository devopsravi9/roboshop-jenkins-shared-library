def info(message) {
    echo "INFO: ${message}"
}

def warning(message) {
    echo "WARNING: ${message}"
}

def hello() {
    pipeline {
        agent any
        satges {
            stage('one') {
                steps {
                    sh 'echo one'
                }
            }
        }
        satges {
            stage('two') {
                steps {
                    sh 'echo two'
                }
            }
        }
    }
}