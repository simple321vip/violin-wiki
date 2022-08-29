package cn.violin.home.book.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Data
@Slf4j
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.pool.max-idle}")
    private int maxIdle;

    @Value("${redis.pool.min-idle}")
    private int minIdle;

    @Value("${redis.pool.max-active}")
    private int maxActive;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.timeout}")
    private int timeout;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);


        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);

        Jedis jedis = jedisPool.getResource();
        log.info(jedis.ping());
        log.info("JedisPool连接成功:" + host + "\t" + port);
        jedis.close();
        return jedisPool;
    }

}
