package study.ywork.netty.eventloop;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * EventLoop维护自己的任务队列，只处理自己的任务，独立于其他的EventLoop对象
 * 性能优化思路就是减少同步处理，对调用线程确认身份，匹配EventLoop线程则执行
 */
public class ScheduleExamples {
    private static final Channel DEMO_CHANNEL = new NioSocketChannel();

    // 使用ScheduledExecutorService调度任务，高负载下性能不佳
    public static void schedule() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> future = executor.schedule(() -> {
            System.out.println("Now it is 60 seconds later");
        }, 60, TimeUnit.SECONDS);
        System.out.println("executor future will handle later " + future);
        executor.shutdown();
    }

    // EventLoop方式调度任务
    public static void scheduleViaEventLoop() {
        Channel ch = DEMO_CHANNEL;
        ScheduledFuture<?> future = ch.eventLoop().schedule(() -> {
            System.out.println("60 seconds later");
        }, 60, TimeUnit.SECONDS);
        System.out.println("eventLoop future will handle later " + future);
    }

    // 重复执行的任务
    public static void scheduleFixedViaEventLoop() {
        Channel ch = DEMO_CHANNEL; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(() -> {
            System.out.println("Run every 60 seconds");
        }, 60, 60, TimeUnit.SECONDS);
        System.out.println("repeat task future will handle later " + future);
    }

    // 取消任务
    public static void cancelingTaskUsingScheduledFuture() {
        Channel ch = DEMO_CHANNEL;
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(() -> {
            System.out.println("Run every 60 seconds");

        }, 60, 60, TimeUnit.SECONDS);
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning);
    }

    private ScheduleExamples() {
    }
}