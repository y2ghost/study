package study.ywork.multi.thread.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultipleFixedRateDemo {
    public static void main(String[] args) throws InterruptedException {
        // 等价于调用函数: Executor.newSingleThreadScheduledExecutor()
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
        service.scheduleAtFixedRate(new MyTask("Task 1"), 500, 1000, TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(new MyTask("Task 2"), 500, 1000, TimeUnit.MILLISECONDS);
        PoolUtil.showPoolDetails((ThreadPoolExecutor) service,
            "For each task" + " initial delay: 500 ms," + " delay period: 1000 ms," + " repeat policy: fixed-rate");
        TimeUnit.SECONDS.sleep(10);
        service.shutdown();
    }
}
