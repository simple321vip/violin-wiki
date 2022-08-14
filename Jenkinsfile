def label = "slave"

podTemplate(label: label, containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6-openjdk-slim', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker:20.10.17-git', command: 'cat', ttyEnabled: true),
], serviceAccount: 'service-jenkins', volumes: [
  hostPathVolume(mountPath: '/home/jenkins/.kube', hostPath: '/root/.kube'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH

    def image = 'ccr.ccs.tencentyun.com/violin/violin-tomcat:v1.00'
    def imageTag = "v1.00"
    def registryUrl = "ccr.ccs.tencentyun.com"
    def docker_user = "10002454003"
    def docker_password = "Mb83201048"
    def imageEndpoint = "violin/violin-book"
    def image = "${registryUrl}/${imageEndpoint}:${imageTag}"

    stage('单元测试') {
      echo "测试阶段"
    }
    stage('代码编译打包') {
      container('maven') {
        echo "代码编译打包阶段"
        sh 'ls'
      }
    }
    stage('镜像构建') {
      withCredentials([[$class: 'UsernamePasswordMultiBinding',
        credentialsId: 'docker-auth',
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASSWORD']]) {
          container('docker') {
            echo "3. 构建 Docker 镜像阶段"
            sh """
              docker login ${registryUrl} --username=${docker_user} -p ${docker_password}
              docker build -t ${image} .
              docker push ${image}
              """
          }
        }
    }
  }
}