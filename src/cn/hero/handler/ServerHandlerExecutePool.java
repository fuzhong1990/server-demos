package cn.hero.handler;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fuzhong on 2015/12/24.
 */
public class ServerHandlerExecutePool {

    private ExecutorService executor;

    public ServerHandlerExecutePool (int maxPoolSize, int queueSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute (Runnable task) {
        executor.execute(task);
    }
}
