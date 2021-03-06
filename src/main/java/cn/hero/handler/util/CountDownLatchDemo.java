package cn.hero.handler.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Fuzhong on 2015/12/24.
 *
 * 这里主线程在所有子线程完成之前一直等待
 *
 * CountDownLatch 类是一个同步计数器,构造时传入int参数,该参数就是计数器的初始值，每调用一次countDown()方法，计数器减1,计数器大于0 时，await()方法会阻塞程序继续执行
 * CountDownLatch 如其所写，是一个倒计数的锁存器，当计数减至0时触发特定的事件。利用这种特性，可以让主线程等待子线程的结束。下面以一个模拟运动员比赛的例子加以说明。
 */
public class CountDownLatchDemo {

    // 运动员人数
    private static final int PLAYER_AMOUNT = 5;

    public CountDownLatchDemo() { }

    public static void main(String[] args) {
        //对于每位运动员，CountDownLatch减1后即结束比赛
        CountDownLatch begin = new CountDownLatch(1);
        //对于整个比赛，所有运动员结束后才算结束
        CountDownLatch end = new CountDownLatch(PLAYER_AMOUNT);
        Player[] plays = new Player[PLAYER_AMOUNT];

        for(int i=0;i<PLAYER_AMOUNT;i++)
            plays[i] = new Player(i+1,begin,end);

        //设置特定的线程池，大小为5
        ExecutorService exe = Executors.newFixedThreadPool(PLAYER_AMOUNT);
        for(Player p:plays)
            exe.execute(p);            //分配线程
        System.out.println("Race begins!");
        begin.countDown();
        try{
            end.await();            //等待end状态变为0，即为比赛结束
        }catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            System.out.println("Race ends!");
        }
        exe.shutdown();
    }
}

class Player implements Runnable {
    private int id;
    private CountDownLatch begin;
    private CountDownLatch end;

    public Player(int i, CountDownLatch begin, CountDownLatch end) {
        super();
        this.id = i;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        try{
            begin.await();        //等待begin的状态为0
            Thread.sleep((long)(Math.random()*100));    //随机分配时间，即运动员完成时间
            System.out.println("Play"+id+" arrived.");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            end.countDown();    //使end状态减1，最终减至0
        }
    }
}
