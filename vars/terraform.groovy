def call() {
    node {
        properties([
            parameters([
                choice(choices: ['dev', 'prod'], description: "choose environment", name: "ENV"),
                choice(choices: ['apply', 'destroy'], description: "choose action", name: "ACTION"),

                ])
        ])
        ansiColor('xterm') {
            stage ('Code Checkout') {
                sh 'ls -al'
                sh 'find . | sed -e "1d" | xargs rm -rf '
                sh 'ls -al'
                git branch: 'main', url: "https://github.com/devopsravi9/${REPO_NAME}.git"
                sh 'exit 2'
            }

            stage('terraform init') {
                sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
            }

            stage ('terraform plan') {
                sh 'terraform plan -var-file=env/${ENV}.tfvars'
            }

            stage ('terraform ${ACTION}') {
                sh 'terraform ${ACTION} -auto-approve -var-file=env/${ENV}.tfvars'
            }
        }
    }
}