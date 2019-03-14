node('SLAVE') {
   stage('Test1') {
      git 'https://github.com/citb33/intros.git'
    }
   stage('Test2') {
      sh 'ls'
    }
}
