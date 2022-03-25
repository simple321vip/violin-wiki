package com.g.estate.school.redis;


/**
 * レプリカ
 * sentinel! redis 从2.x的时候是没有集群的，只有主从复制功能，想要做集群 ，需要借助哨兵机制，3.x有了集群。
 * sentinel 哨兵进程。
 *  通过 ping pong 进行监控
 *  SDOWN 主观下线
 *
 *  哨兵1 超过半数的哨兵进程标记SDOWN则，master被认为ODWON。
 *  哨兵2
 *  哨兵3
 *  过期时间选项：down-after-millionseconds
 *
 *  关于一些缓存击穿 缓存穿透和缓存雪崩
 *
 *
 */
public class SentinelClass {


}
