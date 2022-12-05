package cn.violin.wiki.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//@SpringBootTest
public class JedisApplicationTests {

//    @Autowired
//    private JedisPool jedisPool;

    @Test
    public void contextLoads() {

//        System.out.println(jedisPool);
//        //在连接池中得到Jedis连接
//        Jedis jedis = jedisPool.getResource();
//        jedis.set("haha","你好");
//        jedis.set("name","wangpengcheng");
//        jedis.close();
    }
}
