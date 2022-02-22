package com.g.estate.school;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁，和synchronized一样 也是可重入的锁。
 * 但是synchronized关键字用于加锁，但这种锁一是很重，二是获取时必须一直等待，没有额外的尝试机制。
 * java.util.concurrent.locks包提供的ReentrantLock用于替代synchronized加锁
 * 我们看看源代码ReentrantLock，
 * 几个重要的地方：
 * 第一个 ->>>>>
 * ReentrantLock对象实例化的时候，内部同时实例化一个叫 NonfairSync的锁对象，这可以说是一种委托模式，
 * 从他的顶级父类中AbstractOwnableSynchronizer我可以看到
 *
 * The current owner of exclusive mode synchronization.
 * private transient Thread exclusiveOwnerThread;
 *
 * 第二个->>>>>
 * 他们都是实现了序列化接口java.io.Serializable，
 *
 * 第三个
 * lock()方法调用了NonfairSync的lock方法
 *         final void lock() {
 *             if (compareAndSetState(0, 1))
 *                 setExclusiveOwnerThread(Thread.currentThread());
 *             else
 *                 acquire(1);
 *         }
 *         这个地方 可以看到 继续深挖，看到unsafe.compareAndSwapInt(this, stateOffset, expect, update);
 *         很明显了 这个是典型的CAS操作，对对象，偏移量属性进行内存操作。
 *         stateOffset 是什么？ 这个跟ThreadLocalRandom类一样，通过static代码块利用java反射，从父类AbstractQueuedSynchronizer
 *         类中拿到类型为volatile的state属性的偏移量。
 *         由此可以看出，每一个ReentrantLock都有个NonfairSync对象，NonfairSync中都有一个state属性，
 *         同时把当前的把属性exclusiveOwnerThread赋值当前线程
 *         通过CAS操作来对state
 *         每一个ReentrantLock都有个NonfairSync对象　关于公平锁和不公平锁，在默认是不公平锁
 *
 *         这地方，可以这样理解，执行compareAndSetState去获取锁，如果处于无所状态，自然而然就能获取锁，并将锁的数量state设置为1
 *         如果获取不到，那么调用acquire方法
 *         if (!tryAcquire(arg) &&
 *             acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
 *             selfInterrupt();
 *
 *          因为锁是可重入的，且是有顺序的，
 *             tryAcquire 中，state=0 是去获取锁，如果state!=0则，增加锁的数量。这两种成功都是返回true
 *
 *             addWaiter 方法可以看出 Node是一个FIFO链表结构，有head和tail。也是为了保证线程安全采用了CAS原子操作。
 *
 *         private Node addWaiter(Node mode) {
 *         Node node = new Node(Thread.currentThread(), mode);
 *         // Try the fast path of enq; backup to full enq on failure
 *         Node pred = tail;
 *         if (pred != null) {
 *             node.prev = pred;
 *             if (compareAndSetTail(pred, node)) {
 *                 pred.next = node;
 *                 return node;
 *             }
 *         }
 *         enq(node);
 *         return node;
 *     }
 *
 *
 *
 *
 *
 *
 */
public class ReentrantLockClass {

    private final Lock lock = new ReentrantLock();
}
