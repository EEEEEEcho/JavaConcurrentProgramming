##### StampedLock

该类自 JDK 8 加入,是为了进一步优化读性能,它的特点是在使用读锁、写锁时都必须配合【戳】使用

加解读锁

```java
long stamp = lock.readLock();
lock.unlockRead(stamp);
```

加解写锁

```java
long stamp = lock.writeLock();
lock.unlockWrite(stamp);
```

乐观读,StampedLock 支持 tryOptimisticRead() 方法(乐观读),读取完毕后需要做一次 戳校验 如果校验通
过,表示这期间确实没有写操作,数据可以安全使用,如果校验没通过,需要重新获取读锁(锁升级),保证数据安全。

```java
long stamp = lock.tryOptimisticRead();
// 验戳
if(!lock.validate(stamp)){
    // 锁升级
}

```

提供一个 数据容器类 内部分别使用读锁保护数据的 read() 方法,写锁保护数据的 write() 方法

```java
@Slf4j(topic = "c.DataContainerStamped")
public class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data){
        this.data = data;
    }

    public int read(int readTime) throws InterruptedException {
        //乐观读锁(只要戳没有改变，实际上就没有加锁)
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking ... {}",stamp);
        TimeUnit.SECONDS.sleep(readTime);
        if (lock.validate(stamp)){
            //如果验证戳成功，证明没有被改写过，
            log.debug("read finish ... {}",stamp);
            //返回数据
            return data;
        }
        //否则的话，就证明有过读取，进行锁升级
        log.debug("updating to read lock ...{}",stamp);
        try {
            //加锁
            stamp = lock.readLock();
            log.debug("read lock {}",stamp);
            TimeUnit.SECONDS.sleep(readTime);
            log.debug("read finish ...{}",stamp);
            return data;
        }
        finally {
            log.debug("read unlock {}",stamp);
            //解锁
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData){
        long stamp = lock.writeLock();  //获取戳，加锁
        log.debug("write lock {}",stamp);
        try {
            //模拟写的过程
            TimeUnit.SECONDS.sleep(2);
            this.data = newData;
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            log.debug("write unlock {}",stamp);
            lock.unlockWrite(stamp);
        }
    }
}
```

测试 读-读 可以优化

```java
@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped container = new DataContainerStamped(1);
        new Thread(() -> {
            try {
                container.read(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            try {
                container.read(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
```

输出结果,可以看到实际没有加读锁

```bash
15:20:26 【t1】 c.DataContainerStamped - optimistic read locking ... 256
15:20:26 【t2】 c.DataContainerStamped - optimistic read locking ... 256
15:20:26 【t2】 c.DataContainerStamped - read finish ... 256
15:20:27 【t1】 c.DataContainerStamped - read finish ... 256
```

测试 读-写 时优化读补加读锁

```java
@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) throws InterruptedException {
        DataContainerStamped container = new DataContainerStamped(1);
        new Thread(() -> {
            try {
                container.read(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            try {
                container.write(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
```

输出结果

```bash
15:41:32 【t1】 c.DataContainerStamped - optimistic read locking ... 256	#读线程启动，先以乐观读方式进行读取
15:41:33 【t2】 c.DataContainerStamped - write lock 384 	#0.5后，写线程启动，加锁，设置stamp为384
15:41:33 【t1】 c.DataContainerStamped - updating to read lock ...256	#等待一段时间后开始读取，发现stamp不对，升级至读锁
15:41:35 【t2】 c.DataContainerStamped - write unlock 384	#写线程结束，写锁解锁
15:41:35 【t1】 c.DataContainerStamped - read lock 513	#这时读锁才加上，加上之后stamp为513
15:41:36 【t1】 c.DataContainerStamped - read finish ...513	#完成读取
15:41:36 【t1】 c.DataContainerStamped - read unlock 513	#读锁解锁
```

**注意**

StampedLock不支持条件变量，即不支持wait 和 signal
StampedLock不支持可重入

#### Semaphore

信号量,用来限制能同时访问共享资源的线程上限。可以把semaphore类比为停车场的车位，将线程类比为汽车。

无信号量

```java
@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {
        //1.创建Semaphore对象
        Semaphore semaphore = new Semaphore(3);   //许可为3，表示只能有三个线程可以访问
        //2.创建10个线程来使用
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    log.debug("running....");
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("end....");
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }).start();
        }
    }
}
```

可以看到十个线程同时启动，在一秒之后同时结束

```bash
17:09:08 【Thread-6】 c.TestSemaphore - running....
17:09:08 【Thread-0】 c.TestSemaphore - running....
17:09:08 【Thread-1】 c.TestSemaphore - running....
17:09:08 【Thread-4】 c.TestSemaphore - running....
17:09:08 【Thread-7】 c.TestSemaphore - running....
17:09:08 【Thread-5】 c.TestSemaphore - running....
17:09:08 【Thread-2】 c.TestSemaphore - running....
17:09:08 【Thread-3】 c.TestSemaphore - running....
17:09:08 【Thread-8】 c.TestSemaphore - running....
17:09:08 【Thread-9】 c.TestSemaphore - running....
17:09:09 【Thread-7】 c.TestSemaphore - end....
17:09:09 【Thread-4】 c.TestSemaphore - end....
17:09:09 【Thread-0】 c.TestSemaphore - end....
17:09:09 【Thread-5】 c.TestSemaphore - end....
17:09:09 【Thread-2】 c.TestSemaphore - end....
17:09:09 【Thread-3】 c.TestSemaphore - end....
17:09:09 【Thread-6】 c.TestSemaphore - end....
17:09:09 【Thread-1】 c.TestSemaphore - end....
17:09:09 【Thread-9】 c.TestSemaphore - end....
17:09:09 【Thread-8】 c.TestSemaphore - end....
```

使用信号量

```java
@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {
        //1.创建Semaphore对象
        Semaphore semaphore = new Semaphore(3);   //许可为3，表示只能有三个线程可以访问
        //2.创建10个线程来使用
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    //semaphore --
                    semaphore.acquire();
                    log.debug("running....");
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("end....");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    //semaphore ++
                    semaphore.release();
                }

            }).start();
        }
    }
}
```

将线程限制成了三个一组

```java
17:10:24 【Thread-0】 c.TestSemaphore - running....
17:10:24 【Thread-2】 c.TestSemaphore - running....
17:10:24 【Thread-1】 c.TestSemaphore - running....
17:10:25 【Thread-2】 c.TestSemaphore - end....
17:10:25 【Thread-1】 c.TestSemaphore - end....
17:10:25 【Thread-0】 c.TestSemaphore - end....
17:10:25 【Thread-3】 c.TestSemaphore - running....
17:10:25 【Thread-6】 c.TestSemaphore - running....
17:10:25 【Thread-5】 c.TestSemaphore - running....
17:10:26 【Thread-3】 c.TestSemaphore - end....
17:10:26 【Thread-6】 c.TestSemaphore - end....
17:10:26 【Thread-8】 c.TestSemaphore - running....
17:10:26 【Thread-7】 c.TestSemaphore - running....
17:10:26 【Thread-5】 c.TestSemaphore - end....
17:10:26 【Thread-4】 c.TestSemaphore - running....
17:10:27 【Thread-8】 c.TestSemaphore - end....
17:10:27 【Thread-7】 c.TestSemaphore - end....
17:10:27 【Thread-4】 c.TestSemaphore - end....
17:10:27 【Thread-9】 c.TestSemaphore - running....
17:10:28 【Thread-9】 c.TestSemaphore - end....
```

**应用**

- 使用 Semaphore 限流,在访问高峰期时,让请求线程阻塞,高峰期过去再释放许可,当然它只适合限制单机
  线程数量,并且仅是限制线程数,而不是限制资源数(例如连接数,请对比 Tomcat LimitLatch 的实现)
- 用 Semaphore 实现简单连接池,对比『享元模式』下的实现(用wait notify),性能和可读性显然更好,
  注意下面的实现中线程数和数据库连接数是相等的

**Semaphore版线程池**

```java
@Slf4j(topic = "c.Pool")
class Pool {
    // 1. 连接池大小
    private final int poolSize;
    // 2. 连接对象数组
    private Connection[] connections;
    // 3. 连接状态数组 0 表示空闲, 1 表示繁忙
    private AtomicIntegerArray states;
    private Semaphore semaphore;

    // 4. 构造方法初始化
    public Pool(int poolSize) {
        this.poolSize = poolSize;
// 让许可数与资源数一致
        this.semaphore = new Semaphore(poolSize);
        this.connections = new Connection[poolSize];
        this.states = new AtomicIntegerArray(new int[poolSize]);
        for (int i = 0; i < poolSize; i++) {
            connections[i] = new MockConnection("连接" + (i + 1));
        }
    }

    // 5. 借连接
    public Connection borrow() {// t1, t2, t3
// 获取许可
        try {
            semaphore.acquire(); // 没有许可的线程,在此等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < poolSize; i++) {
// 获取空闲连接
            if (states.get(i) == 0) {
                if (states.compareAndSet(i, 0, 1)) {
                    log.debug("borrow {}", connections[i]);
                    return connections[i];
                }
            }
        }
// 不会执行到这里
        return null;
    }

    // 6. 归还连接
    public void free(Connection conn) {
        for (int i = 0; i < poolSize; i++) {
            if (connections[i] == conn) {
                states.set(i, 0);
                log.debug("free {}", conn);
                semaphore.release();
                break;
            }
        }
    }
}
```

