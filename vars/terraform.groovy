def call() {
    node {
        properties([
            parameters([
                choice(choices: ['dev', 'prod'], description: "choose environment", name: "ENV"),
                ])
        ])
        ansiColor('xterm') {
            stage ('Code Checkout') {
                sh 'rm -rf *'
                sh 'find .  '
                git branch: 'main', url: 'https://github.com/devopsravi9/roboshop-terraform-mutable.git'
            }

            stage('terraform init') {
                sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
            }

            stage ('terraform plan') {
                sh 'terraform plan -var-file=env/${ENV}.tfvars'
            }

            stage ('terraform apply') {
                sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
            }
        }
    }
}