## プログラム紹介
violin-wiki, a backend program to support for violin online edit function.

### kubernetes
we use kubernetes to deployment 
> the violin-wiki is built on kubernetes.
> service, storageclass, pvc, deployment

### framework
> springboot, jedis and so on. for more, we can see pom.xml
 
### about security

> baidu cloud oauth -> token -> redis + token
> redis as a kubernetes service opened only for violin-book application that to save token as a cache one day. 

### server

> the backend server tomcat embedded in the springboot used.

### database
- mysql
  未更新
- mongodb
  基于B-树的文档数据库，各个节点上都存有数据，单次查询速度极快
  dev 環境：
    cd "E:\Program Files\mongodb\bin"
    .\mongod --dbpath "E:\Program Files\mongodb\data\db"
    
  prod 环境：
    docker exec -it containerid /bin/sh
    mongo
    db.collection.find().pretty()
    db.collection.find({key1:value1})
    db.collection.update( { "account" : "simple321vip" } , { $set : { "wikiName" : "guan"} } );
    
    
- redis
  as a cache database, we use it to save token.
  
    // use redis-cli
    docker exec -it containerId /bin/sh
    cd /usr/local/bin/
    redis-cli

    // set auth password
    config get requirepass
    config set requirepass 123456
    
    // 验证密码是否正确
    auth 123456

    // 切换到指定的数据库
    select 3 
