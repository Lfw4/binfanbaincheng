package test.num6;

import java.util.concurrent.atomic.AtomicStampedReference;

import static java.lang.Thread.sleep;

/**
 * @author刘锋伟
 * @date 2020/6/4 14:46
 *
 * 乐观锁通过AtomicStampedReference来保证能够知道是否已经有其他线程进行更改该过.并且改过几次
 */
public class _02 {
    static AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("A", 1);
    public static void main(String[] args) throws InterruptedException {
        //获取值
        String prev = atomicStampedReference.getReference();
        //获取版本号
        int stamp = atomicStampedReference.getStamp();
        other();
        sleep(100);
        System.out.println(atomicStampedReference.compareAndSet(prev, "C", stamp, stamp + 1));
    }
    public static void other() throws InterruptedException {
        new Thread(()->{
            atomicStampedReference.compareAndSet
                    (atomicStampedReference.getReference(),
                            "B",
                            atomicStampedReference.getStamp(),
                            atomicStampedReference.getStamp() + 1);
        }, "t1").start();
        sleep(50);
        new Thread(()->{
            atomicStampedReference.compareAndSet
                    (atomicStampedReference.getReference(),
                            "C",
                            atomicStampedReference.getStamp(),
                            atomicStampedReference.getStamp() + 1);
        }, "t2").start();
    }
}
