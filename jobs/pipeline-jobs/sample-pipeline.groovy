node('SLAVE') {
    stage('Build') {
        echo 'Hello World'
        sh '''uptime
ls
pwd'''
        
    }
    
    stage('Build2') {
        echo 'Hello World- Build2'
    }
    
    stage('BUILD3') {
        echo 'Hello BUILD3'
    }
}
