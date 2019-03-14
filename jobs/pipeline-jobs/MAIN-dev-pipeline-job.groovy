node('SLAVE') {
  stage('CLONE') {
     git 'https://github.com/citb33/studentproj-code.git' 
   }

  stage('COMPILE'){
     sh 'mvn compile -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
   }

  stage('Code Quality Check') {
    sh 'mvn sonar:sonar -Dsonar.projectKey=citb33_studentproj-code -Dsonar.organization=citb33 -Dsonar.host.url=https://sonarcloud.io   -Dsonar.login=24dc0afa2658cdc57cdce6f5d3d7538f3ded4276 -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
  }

  stage('Packaging'){
    sh 'mvn package -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
  }
  
  stage('Upload Artifacts') {
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'NEXUS-USER-PASS', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh 'mvn deploy -D NEXUS_USERNAME=$USERNAME -D NEXUS_PASSWORD=$PASSWORD -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
        }
    
  }
}
