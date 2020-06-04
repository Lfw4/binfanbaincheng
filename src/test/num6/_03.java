package test.num6;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author刘锋伟
 * @date 2020/6/4 15:03
 *
 * 用来判断某个值是否被更改过, 只通过boolean类型来进行判断
 */
public class _03 {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("装满了垃圾袋");
        //参数2 mark 可以看作是一个标记，表示垃圾袋满了
        AtomicMarkableReference<GarbageBag> garbage = new AtomicMarkableReference<GarbageBag>(bag, true);
        GarbageBag reference = garbage.getReference();

        //中途将标记更换为false
        new  Thread(()->{
            bag.setDesc("空垃圾袋");
            garbage.compareAndSet(bag, bag, true, false);
            System.out.println(bag.toString());
        }).start();

        Thread.sleep(100);
        boolean success = garbage.compareAndSet(reference, new GarbageBag("空的垃圾袋"), true, false);
        System.out.println("是否换了" + success);
        System.out.println(garbage.getReference().toString());
    }
}
class GarbageBag{
    String desc;

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "GarbageBag{" +
                "desc='" + desc + '\'' +
                '}';
    }
}