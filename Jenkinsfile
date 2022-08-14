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

    stage('单元测试') {
      echo "测试阶段"
    }
    stage('代码编译打包') {
      container('maven') {
        echo "代码编译打包阶段"
        sh 'mvn install'
        sh 'ls'
      }
    }
    stage('镜像构建') {
      container('docker') {
        echo "镜像构建阶段"
        script {
          dockerImage = docker.build + "violin-book:v1.00"
        }
      }
    }
  }
}