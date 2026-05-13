package study.ywork.multi.thread.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SingleFixedDelayDemo {
    public static void main(String[] args) throws InterruptedException {
        // 等价于调用函数: Executors.newSingleThreadScheduledExecutor()
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleWithFixedDelay(new MyTask("Task 1"), 500, 1000, TimeUnit.MILLISECONDS);
        PoolUtil.showPoolDetails((ThreadPoolExecutor) service,
            "For each task" + " initial delay: 500 ms," + " delay period: 1000 ms," + " repeat policy: fixed-delay");
        TimeUnit.SECONDS.sleep(10);
        service.shutdown();
    }
}
