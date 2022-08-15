def label = "slave-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
  containerTemplate(name: 'maven', image: 'maven:3.6.3-openjdk-11-slim', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker:20.10.17-git', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'kubectl', image: 'bitnami/kubectl:1.23.7', command: 'cat', ttyEnabled: true)
], serviceAccount: 'jenkins-admin', volumes: [
  hostPathVolume(mountPath: '/home/jenkins/.kube', hostPath: '/root/.kube'),
  hostPathVolume(mountPath: '/root/.m2', hostPath: '/root/.m2'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
    def myRepo = checkout([
      $class: 'GitSCM',
      branches: [[name: "*/dev"]],
      doGenerateSubmoduleConfigurations: false,
      extensions:  [[$class: 'CloneOption', noTags: false, reference: '', shallow: true, timeout: 1000]]+[[$class: 'CheckoutOption', timeout: 1000]],
      submoduleCfg: [],
      userRemoteConfigs: [[
        credentialsId: '2448e943-479f-4796-b5a0-fd3bf22a5d30',
        url: 'https://gitee.com/guan-xiangwei/violin-book.git'
        ]]
      ])


    def gitCommit = myRepo.GIT_COMMIT
    def gitBranch = myRepo.GIT_BRANCH

    def imageTag = "v1.00"
    def registryUrl = "ccr.ccs.tencentyun.com"
    def imageEndpoint = "violin/violin-book"
    def image = "${registryUrl}/${imageEndpoint}:${imageTag}"

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
      withCredentials([[$class: 'UsernamePasswordMultiBinding',
        credentialsId: '8eb5126b-6a2f-4644-add0-bc2a669e663d',
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASSWORD']]) {
          container('docker') {
            echo "3. 构建 Docker 镜像阶段"
            sh """
              docker login ${registryUrl} --username=${DOCKER_USER} -p ${DOCKER_PASSWORD}
              docker build -t ${image} .
              docker push ${image}
              """
          }
        }
    }
    stage('预发布') {
      container('kubectl') {
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
          echo "查看 K8S 集群 Pod 列表"
          sh "mkdir -p ~/.kube && cp ${KUBECONFIG} ~/.kube/config"
          sh "kubectl get pods"
        }
      }
    }
  }
}