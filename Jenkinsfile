pipeline{
    agent : any


    stages{
        stage("Verify Branch"){
            steps{
                echo "$GIT_BRANCH"
            }
        }
        stage("Docker BUILD"){
            steps{
                pwsh(script: "docker images -a")
                pwsh(script: """
                    cd myApp/
                    docker images -a 
                    docker build -t jenkins/jenkins .
                    docker images -a 
                    cd ..
                    """)
            }
        }
    }
}