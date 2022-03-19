package com.g.estate.school.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * five kinds of date.
 * string
 * hash
 * list
 * set
 * sortedSet
 *  应用场景：
 *      内存数据库
 *      缓存
 *      解决分布式集群中的session分离问题
 *      任务队列，秒杀，抢购，12306 ---单线程的
 *      支持消息订阅的消息模式
 *      应用排行榜
 *      网站访问统计
 *      数据过期处理
 * linux install command
 *  1.　yum install -y gcc-c++
 *  2. tar -zxf
 *  3. make
 *  4. make install prefix=installPath
 *  5. cp /kkb/redis6/redis.conf /kkb/redis/bin/ 配置ファイルをbinの配下にコピー、redis.conf　修正
 *
 *  sync: setnx key value , 只有第一次对key赋值的时候才会返回1 其余时候都是返回0
 *  キャッシュとしてよく使われているコマンドは　
 *  expire key seconds キャッシュ有効期限
 *  ttl key　キャッシュ有効期限までの残るタイム
 *  persist key　キャッシュ有効期限を削除
 *
 *  トランザクション　コマンド
 *  multi トランを開始
 *  exec トランを実行
 *  discard トランをキャンセル
 *  unwatch key
 *  watch key
 *  ※　トランザクションのロールバックを支持しない
 *  なぜ　ですか？
 *  エラータイプ：　RunTime error と　compile error
 *  compile error の場合は　exec実行しても、実際実行してないです。
 *  RunTime error の場合は　exec実行できた、エラーが発生したところは、無視
 *
 *  分布式锁，多进程锁
 *
 *  - 互斥性，任何时刻，只有一个客户端持有锁，
 *  - 同一性，开锁和解锁必须是一个人
 *  - 避免死锁
 *
 *      set 和 setnx
 *
 *
 *
 *
 */
public class RedisClass {

    private static JedisPool jedisPool;

    private final static String HOST = "192.168.112.130";
    private final static String PORT = "6379";

    static {
        // シングル　クライアント　は実際使わないです。
        jedisPool = new JedisPool();

    }

    /**
     * ロックを取得する
     * @param lockKey
     * @param requestId IP　など、クラスターなかで唯一の認識番号
     * @return 成功　あるいは　失敗
     */
    public static boolean getLock(Jedis jedis, String lockKey, String requestId, int timeout) {
        SetParams setParams = new SetParams();
        setParams.nx().ex(timeout);

        String result = jedis.set(lockKey, requestId, setParams);
        System.out.println(result);
        if (result == "OK")
            return true;
        return false;
    }

    public static void releaseLock(Jedis jedis, String lockKey, String requestId) {
        if (requestId.equals(jedis.get(lockKey))) {
            jedis.del(lockKey);
        }
    }

    public static void main(String[] args) {
        Jedis jedis = jedisPool.getResource();
        jedis.connect();
        String lockKey = new Random().toString(); // 如果存在 则返回0 即false 如果不存在则返回 1 true
        String requestId = "192.168.112.130"; // ip address
        int timeout = 1200; // 设置超时时间 防止死锁
        boolean lock = getLock(jedis, lockKey, requestId, timeout);
        if (lock) {

        }
        releaseLock(jedis,lockKey,requestId);


        jedis.close();
        jedisPool.close();
    }

}
