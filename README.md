## About
violin-wiki, a backend program to support for violin online edit function.

### database

    
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
