package test.num7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author刘锋伟
 * @date 2020/6/4 15:35
 *
 * 该类用来模拟数组的原子性
 */
public class _01 {
    public static void main(String[] args) {
        demo(
                () -> new int[10],
                array -> array.length,
                (array, index) -> array[index]++,
                array -> System.out.println(Arrays.toString(array))
        );
        demo(
                () -> new AtomicIntegerArray(10),
                array -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                array -> System.out.println(array)

        );

    }

    /**
     *
     * @param arraySupper 提供数组，可以在线程不安全数组或线程安全数组
     * @param lengthFun 获取数组长度的方法
     * @param putConsumer 自增方法，回传array， list
     * @param printConsumer 打印方法
     * @param <T>
     */
    private static<T> void demo(
            Supplier<T> arraySupper,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer)
    {
        List<Thread> list = new ArrayList();
        T array = arraySupper.get();
        int len = lengthFun.apply(array);
        for (int i = 0; i < len; i++) {
            list.add(new Thread(() ->{
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % len);
                }
            }));
        }
        list.forEach(Thread :: start);
        list.forEach(t ->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
