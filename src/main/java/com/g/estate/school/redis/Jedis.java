package com.g.estate.school.redis;

/**
 * https://blog.51cto.com/happytree007/2586641  この文章はすごく詳しくて、見やすいし
 *
 * redis log
 *  デフォルトはログファイルはないですが、パスを追加します
 *  logfile "/var/log/redis.log"
 *  原因わからない場合は、これを参照します。
 *
 * IP　ADDRESS　BIND
 * redis.conf ファイルを　bind 127.0.0.1 为 bind ローカルIP
 *
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
 *  主从复制 原理
 *  https://blog.51cto.com/happytree007/2586641
 *  マスタ　replica のこと：
 *          主从架构 ->
 *              读写分离 ->
 *                  水平扩容支撑读高并发，主节点用来写，从节点用来读
 *
 *      replica node 起動　⇒　リクエスト　（PSYNC）　⇒　マスタ　
 *          もし、初回接続の場合は、full resynchronizationフルコピーを行う。
 *              この時、同時二つのことを行う。
 *                  １，マスタはもう一つのバックエンドスレッドを起動して、RDBファイルを作成する
 *                  ２、クライアントからのリクエスト（トランザクション書く）をメモリーに一時保存する
 *          RDBが出来たら、RDBファイルをreplicaに送信します。replica は受信して、RDBをルーカルハードディスクに書き込みとメモリに読み込み。
 *          ↑が完了すると、２でマスタのメモリーに保存されたデータもreplicaに送信します。
 *
 *          もし、replicaがマスタのネットワークから切れた場合も、足りない部分をマスタから取得し、データを一致にします。
 *
 *      ↑は基本の設計ですが、業務によて、カスタマイズできます。
 *
*          PSYNC格式：
*              PSYNC runid offset
*              runid　身分証明書　これが保存しない場合　？　で　offset 1とする。なのでこの場合は　PSYNC ? 1 意味は　全量复制
*              offset　両方はoffsetを持っています。
 *
 *         全量复制流程　ー＞
 *              マスタは　PSYNC ? 1　の受信を受けると、bgsave を実行して、RDBファイルを生成します。かつ、メモリーに特別キャッシュを利用して、bgsaveを実行
 *              後時点から、書くリクエストはRDBに書けないです、原因はマルチスレッドは通信してないからです。
 *              redis.conf ファイルにclient-output-buffer-limit replica 256MB 64MB 60　設定できます。
 *              特別キャッシュ　このルールは業務に合わせるように変更すればいいです。
 *         部分复制流程（增量复制）　ー＞
 *              master 直接从自己的 backlog 中获取部分丢失的数据，发送给 replica node，默认 backlog 就是 1MB
 *              master 就是根据 replica 发送的 psync 中的 offset 来从 backlog 中获取数据的。
 *
 *         https://blog.csdn.net/weixin_34284188/article/details/91459657
 *
 *         主从复制的断点续传　redis 2.8以降は、追加機能
 *
 *         无磁盘化复制
 *              master 在内存中直接创建 RDB，然后发送给 replica，不会在自己本地落地磁盘了。只需要在配置文件中开启 repl-diskless-sync yes 即可。
 *
 *         过期key处理
 *              replica不会过期key，只会等待master过期key。如果master过期了一个key，或者通过LRU淘汰了一个key，那么会模拟一条del命令发送给replica。
 *         heartbeat
 *              master 默认每隔 10秒 发送一次 heartbeat，replica node 每隔 1秒 发送一个 heartbeat
 *
 *
 *
 *         master持久化对于主从架构的安全保障的意义
 *              如果采用了主从架构，那么建议必须开启master node的持久化！
 *
 *
 *
 */
public class Jedis {
}
