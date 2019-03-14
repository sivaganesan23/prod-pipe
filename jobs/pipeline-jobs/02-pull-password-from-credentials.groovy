node('SLAVE') {
    stage('Variables') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'SAMPLE-USER-PASS', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh 'echo uname=$USERNAME pwd=$PASSWORD >/tmp/abc'
        }
    }
}