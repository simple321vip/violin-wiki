## Zookeeper紹介　###

- 应用程序协调服务器
- 为分布式服务提供一致性服务
- 一致性是通过Paxos算法[ˈpæksoʊs]的ZAB协议完成。
- 主要功能：配置维护，域名服务，分布式同步，集群管理

### 主要功能

#### ①- 配置维护
    hadoop 上千台集群服务器，维护配置，利用发布订阅模式。
    发布者将修改好的配置信息，发布到zookeeper服务器，那么订阅者马上接收到通知，并主动同步Zookeeper配置文件。
    Zookeeper具有同步操作的原子性（ZAB协议的核心），确保每个集群服务器的配置文件都能被正确修改。
   
#### ② - Naming Service 域名服务
    
    微服务中，多个服务将自己注册到Zookeeper中，服务映射表， 消费者只需要从表中拿取服务消费即可。
    服务的增加减少表更，只通过就该服务映射表即可。
    阿里的dubbo就是使用zookeeper作为服务域名服务器的。
 
#### ③ - 分布式同步

    协调服务的运算过程，比如处理的先后顺序协调
    让服务器监听zookeeper上一个znode节点（数据存储节点），一旦一个服务器更新了节点，
    那么其他服务器就会接收通知，做相应处理。

#### ④ - 集群管理

    zookeeper 会让集群选出一个健康的节点，作为master，master随时监控每个节点的健康状况
    一旦某个节点发生故障，master会立即通知其他节点，使其他节点对任务的分配做出相应的处理。
    master故障了，zookeeper也有选举算法，选出新的master。


### 一致性要求

    - 顺序一致性 
        从同一个客户端发起的n个事务请求（写），最终会严格按照其发起顺序被应用到zookeeper中
    - 原子性
        所有发起的请求事务，在所有主机中要么都应用了事务，要么都没应用了事务
    - 单一试图
        无论客户端链接的是哪一个zookeeper服务器，其看到的服务端的数据模型都是一致的。
    - 时效性
        zookeeper 只能保证一段时间内客户端能够读到最新的数据状态。
   
### 重要概念
    - session
        session 是指客户端会话，zookeeper的对外服务端口是2181，客户端启动时会和zk建立TCP长连接
        通过心跳检测，保持链接。设置超时时间，不超过超时时间，会话还是会保持的。
        
    - Znode
        树型节点。客户端可以在znode上设置Watcher监视器
        
    - Watcher
        zk通过该机制实现了发布订阅模式。
        zk允许客户端注册一个watcher监听服务端状态。
        
    - ACL access control list 访问控制列表
        即权限，但zk 中的 ACL（和传统的文件系统相比）是没有继承关系的。
        传统的是2个维度，组和权限
        而zk是三个维度：授权策略schema，用户id，权限permission
        
### PAXOS算法
    
    基于消息传递的，一致性算法。
    白厅将军问题，如果存在存在消息丢失，那么通过消息传递方式达到一致性是不可能。
    即 PAXOS的前提是 通信道路是安全的，可靠的，节点间传递的消息不会被串改。
    
    一般情况下，分布式消息通讯，有两种方式，一种是内存共享和消息传递
    内存共享 风险很高。
    PAXOS zk中采用的是消息传递
    
###### 算法描述
        该算法有三种角色： 当然角色是可以同时拥有的
            提议者 Proposer
            表决者 Acceptor
            学习者 learner
        
        提案 具有全局唯一性。提案编号具有全局唯一性。
        表决者accept提案时，会将最大提案编号保存在本地，
        最终只能有一个提案被选中。
        超过半数，才有被选中的可能。
        一旦提案被选定，其他服务器会主动同步提案（learn）
###### 算法过程
        
     PAXOS 算法分为两个阶段： prepare阶段 accept阶段
        
    A - prepare 阶段
        Proposer 准备提交一个编号为N的提议，于是向accepter发送prepareN的请求，用于试探集群是否支持该编号的提议
        每个accepter都保存着曾经accept过的最大的编号maxN，
            如果prepareN的请求，N 小于等于maxN 说明过时，acceptor拒绝该提案（不回消息，或者ERROR信息）
            如果prepareN的请求，N 大于maxN时，说明提案时可行的，acceptor将手中最大的maxN反馈
            给Proposer（mid， maxN， value），并提供建议。
                mid表示Acceptor 的 标识id，maxN表示曾经接受过的最大提案编号，第三个value表示填内容
    B - accept 阶段
        如果prepareN的反馈超过半数，则会将该提案（提案编号N，提案内容value）发给全部的acceptor。
        如果 acceptor 接收到N和maxN对比，N 大于maxN或者prepareN时候的N时，则可以接受，否则则拒绝。
        如果 prepare（N，value） 未通过，则重新进入prepare阶段，递增提案编号。           
        如果 prepare（N，value） 通过，所有的acceptor都会成为learner。
        
        
#### CAP 原则
    CAP相对于 关系型数据库的ACID这样的概念提出来的。
    CAP作为原则或者说是定理是在分布式系统中提出来的。
    - C Consistency 一致性 在分布式系统中的所有数据备份，在同一时刻是否同样的值
    - A Availability 可用性 保证每个请求不管成功或者失败都有响应
    - P Partition tolerance 分区容错性 系统中任意信息的丢失或失败不会影响系统的继续运作。
    CAP原则：以上三个在分布式系统中最多能实现2个，不可能算个同时实现。
    基于以上的原则，Zookeeper 是保证CP 原则的，即不保证可用性，因为Zookeeper在选举过程中，是不接受外部的请求的。
    相反SpringCloud中的eureka采用的是AP原则，即保证可用性和分区容错性。
     
#### ZAB 协议
    ZAB，Zookeeper Atomic Broadcast 原子消息广播协议，是PAXOS算法的优化，ZAB协议是用来实现C，即数据一致性的。
    崩溃恢复的原子广播协议
    zk 主备模型，（Leader Follower），只有Leader负责写请求事务，其他Follower遇到写请求，会转发给Leader
    ZAB 协议的三个角色：
        Leader 集群写请求处理者。Leader会广播提出提议Proposel，只有大多数zkServer同意时，才会更改。
        Follower 处理读请求，对写请求转发给Leader，在选主过程中参与投票。 这个上升到政治高度就是说，他是本国国籍的，有选举权和被选举权。
        Observer 其存在只是为了协助读请求，没有选举权和被选举权，比如在别的国家打工一个道理。
            增加Observer可以增加读的吞吐量。
    ZAB 协议的三种模式：
        恢复模式，同步模式，广播模式
        - 恢复模式
            服务重启过程中，或者Leader崩溃后，就进入恢复模式。
        - 同步模式
            Leader被选举后，各个Follower需要将Leader中的数据同步到自己，超过半数同步完后即，zkServer整体就可以对外工作了。
        - 广播模式
            Leader的Proposel被半数zkServer同意后，Leader会修改自身数据，然后将修改后的数据广播给其他Follower
            
    zxid 
        64位的long（java）类型 其中
            高32位- epoch 纪元，朝代我觉得理解会更好，leader就是一个朝代，换leader后就会产生新的朝代。
            低32位- xid 事务标识 每一次写请求都是一次事务，每次事务都需要提案投票。
    
    消息广播算法
        跟上面说的有点重复了，该算法主要还是说，写事务请求都会由Leader来做，但是Leader 需要将该次写请求，通过广播投票的方式进行表决。
        Leader 通过zxid递增的方式对写事务管理
        为了保证向Follower的提案是有序的，Leader 会为每一个Follower创建利用FIFO队列管理。并将提案副本写入队列。
        Follower接受提案后，会与本地事务日志中的最大zxid相比较，当该Proposel大于本地zxid时，将该提案提交到本地日志中，并向Leader返回个ACK
        当Leader收到超过半数ACKs后，Leader就会向所有Follower提出Commit操作。
    
    恢复模式的两个原则：
        
        - 已被处理过的消息不能丢，即只要任意一台zkServer提交了事务。
            我们知道，提交过事务的Follower 事务日志中zxid相比其他没执行的zkServer来讲，他是最大的.
            选Leader时候，是选zxid最大的，所以，只需要同步就可以把事务在广播出去。
        - 被丢弃的消息不能被提案， 即没有被zkServer提交过的事务，就不要了，要删掉。
        通过以上两个原则保证一致性。
     
    Leader选举算法：
        基本相同。Leader 至少需要两台主机，
        zkServer的状态： LOOKING FOLLOWING OBSERVING LEADING
                
        - 重启服务中的leader选举
            1 每个server发起一次投票：
            2 第一次都给自己投票，并发生广播（myid， zxid），状态为LOOKING。
            3 每个Server都接受各个服务器的投票。
            4 处理投票
                进行选举，优先选择zxid比较大的。zxid比较大，要么是epoch比较大，要么是同epoch的xid比较大的。为了保证同步是最新的。
                如果zxid相同，说明大家都是一样的状态，这样认为规定，选择最大的myid或者最小的myid，这个认为定下规则即可。
                myid小的Server，发现自己不适合做Leader，然后就更新投票，重新广播投票，
                myid大的Server，无需更新，再次广播投票信息即可。
            5 投票统计
                超过半数zkServer的投票信息相同，就可以选出新的Leader了。
            6 更新状态，同步状态
  
        - Leader挂了后的Leader选举
            1 Leader崩溃后，回暂停服务提供。
            2 所有服务器更新状态为LOOKING
            3 每一个SERVER投票选自己并广播。
            其实大体上和重启的Leader选举是一样的。
         
    恢复数据时的数据同步：
        Leader会为每一个Follower装备一个提案Proposel队列。每个Proposel后会跟一个commit消息。
        Foolwer接受事务请求并直接执行后，向Leader发送ACK请求，Leader会将Follwer加入到可用的follwer队列。    
                

