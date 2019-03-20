node('SLAVE') {
   stage('CLONE') {
    dir('APPCODE') {
      git 'https://github.com/sivaganesan23/studentproj-code.git' 
    }
   }

  stage('COMPILE'){
    dir('APPCODE') {
     sh 'mvn compile -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
    }
   }

  stage('Code Quality Check') {
    dir('APPCODE') {
      sh 'mvn sonar:sonar -Dsonar.projectKey=sivaganesan23_studentproj-code2 -Dsonar.organization=sivaganesan23-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=e2c37a6baa4f87bc300e98c1d3d5abc7fbcab70a -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
    }
  }

  stage('Packaging'){
    dir('APPCODE') {
      sh 'mvn package -D VERSION=$VERSIONNO -D TYPE=$VERSIONTYPE'
    }
  }
  stage('TEST-ENV-CREATE') {
    withCredentials([file(credentialsId: 'CENTOS-USER-PEM-FILE', variable: 'FILE')]) {
      sh '''cat $FILE >/home/centos/devops.pem
chmod 600 /home/centos/devops.pem
'''
    }
    try {
          dir('TERRAFORM') {
            git 'https://github.com/sivaganesan23/prod-pipe.git'
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'TERRAFORM-KEYS', usernameVariable: 'ACCESS_KEY', passwordVariable: 'SECRET_KEY']]) {
                sh '''
      export AWS_ACCESS_KEY_ID=$ACCESS_KEY
      export AWS_SECRET_ACCESS_KEY=$SECRET_KEY
      export AWS_DEFAULT_REGION=us-east-2
      cd stack-test-env
      terraform init
      terraform apply -auto-approve
      '''
              }
          }
          }
    } catch(Exception ex) {
      sh 'rm -f /home/centos/devops.pem'
    }

    try { 
      dir('ANSIBLE') {
         git 'https://github.com/sivaganesan23/prod-pipe.git'
          withCredentials([file(credentialsId: 'CENTOS-USER-PEM-FILE', variable: 'FILE')]) {
        sh '''
        cat $FILE >/home/centos/devops.pem
        chmod 600 /home/centos/devops.pem
        ansible-playbook -i /tmp/hosts ansible_pull/deploy.yml
        '''
          }
      }
    } catch(Exception ex) {
      sh 'rm -f /home/centos/devops.pem'
      
    } 
  }
   stage('UI TESTING') {
      dir('selenium') {
        git 'https://github.com/sivaganesan23/prod-pipe.git'
        sh ''' 
        cd selenium
        PUBLIC_IP=$(cat /tmp/pubip)
        sed -i -e "s/IPADDRESS/$PUBLIC_IP/" src/test/java/framework/CrudStudent.java
        mvn clean install "-Dremote=true" "-DseleniumGridURL=http://sivaganesan23:14578b4e-f344-4872-9bbb-a265412e4019@ondemand.saucelabs.com:80/wd/hub" "-Dplatform=Windows" "-Dbrowser=Chrome" "-Dbrowserversion=51" "-Doverwrite.binaries=true"
          '''    
              }
           }
        } 
