## プログラム紹介
violin-book, a backend program to support for violin online edit function.

### kubernetes
we use kubernetes to deployment 
> the violin-book is built on kubernetes.
> service, storageclass, pvc, deployment

### framework
> springboot, jedis and so on. for more, we can see pom.xml
 
### about security

> baidu cloud oauth -> token -> redis + token
> redis as a kubernetes service opened only for violin-book application that to save token as a cache one day. 

### server

> the backend server tomcat embedded in the springboot used.

### database
- h2
  java嵌入式数据，用于master数据的存储。 
  ip + port + context-path + url
  localhost:8081/h2-console データベースUI画面
- mongodb
  基于B-树的文档数据库，各个节点上都存有数据，单次查询速度极快
  dev 環境：
    cd "E:\Program Files\mongodb\bin"
    .\mongod --dbpath "E:\Program Files\mongodb\data\db"
- redis
  as a cache database, we use it to save token.
  
  docker exec -it containerId /bin/sh
  cd /usr/local/bin/
  
  config get requirepass
  config set requirepass 123456
  
### 功能介绍

- blog
  基本功能完成

- bookmark
  基本功能完成, 完善中
  
- onenote部分
  建设中，
  
- 认证登陆部分
  部分完成
  
- 项目部署
  基于kubernetes  

### 

 設計書書く
 https://app.diagrams.net/