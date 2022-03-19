package com.g.estate.school.redis;

/**
 * redis log
 *  デフォルトはログファイルはないですが、パスを追加します
 *  logfile "/var/log/redis.log"
 *  原因わからない場合は、これを参照します。
 * redis 持久化方案
 * RDB方式 默认
 * 通过快照的方式实现持久化。
 * 快照条件：
 *  - 自定义快照场景， 自动执行
 *      save seconds changes ，满足seconds 发生changes 就执行快照
 *      redis.conf 中的多个快照条件时或的关系
 *      RDBファイル居場所は dir ./ 変更か可能となります。
 *      １GB的快照时间也就20s左右
 *
 *  - 执行save bgsave 命令 手动
 *  - 执行 flashall 命令 手动
 *  - 执行主从复制 手动
 *
 *  快照原理：
 *      1 redis 使用 fork function复制一份当前进程的副本（子进程）
 *      2 父进程继续工作，而子进程将内存中的数据写入磁盘中的临时文件
 *      3 当，写操作完事后，会将该临时文件替换原有的快照。
 *      RDB文件是二进制文件
 *      缺点：异常退出后，最后一次快照以后的数据会丢失
 *      对于缓存，随便丢，缓存是数据库的副本。
 *
 * AOF
 *  写操作，会将该数据持久化到硬盘，会降低redis性能，但是通过提高硬盘可以减少影响。
 *  通过 修改 appendonly yes参数 来开启
 *  默认名字是appendonly.aof 但是不是二进制的
 * redis 默认是0 数据库
 * 默认是16个数据库
 * database 可以修改
 * aof 是有重写过程的，这个过程是绝对安全的。只有新的写完了才会切换到新的。
 * 理论上在磁盘缓存中也会有可能出现数据丢失
 * aof 重写策略
 *  auto-aof 可以查询到。
 *  同步磁盘缓存
 *      appendfsync always 最安全，效率最低
 *      appendfsync everysec 每一秒执行   最常用
 *      appendfsync no
 *  aof 损坏如何修复
 *      1 backup aof文件
 *      2 使用 redis-check-aof程序对aof进行 修复
 *      3 restart redis，wait！
 *
 *  redis 主从复制
 *      主从复制保证了高可用。
 *       一个redis 既可以是主也可以是从
 *      需要改从配置
 *      以前版本是slave  现在的版本是  replica
 *
 *      protect-modeを注意してください
 *
 *
 *
 *
 *
 */
public class Jedis {
}
