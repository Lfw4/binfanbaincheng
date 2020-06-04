package test.num6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author刘锋伟
 * @date 2020/6/4 13:29
 *
 * 该方法用来测试小数的减法原子性
 */

public class _01 {
    public static void main(String[] args) {
        //传递字符串，如何传递小数仍然有精确文件
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal("10000")));
    }
}
class DecimalAccountCas implements DecimalAccount{
    private AtomicReference<BigDecimal> balance;


    public DecimalAccountCas(BigDecimal balance) {
        this.balance = new AtomicReference<BigDecimal>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while(true){
            BigDecimal bigDecimal = balance.get();
            BigDecimal subtract = bigDecimal.subtract(amount);
            if (balance.compareAndSet(bigDecimal, subtract)) {
                break;
            }
        }
    }
}


interface DecimalAccount{
    //获取余额
    BigDecimal getBalance();

    //取款
    void withdraw(BigDecimal amount);

    /**
     * 方法内会启动1000个线程，每个线程做-10元操作。
     * 如何初始余额为1000 那么正确结果应该是0
     */
    static void demo(DecimalAccount account){
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
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
        System.out.println(account.getBalance());
    }

}