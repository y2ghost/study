package study.ywork.multi.thread;

import java.util.ArrayList;
import java.util.List;

/*
 * 后台线程示例
 */
public class MemoryWatcherDemo implements Runnable {
    private static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        MemoryWatcherDemo.start();
        for (int i = 0; i < 100000; i++) {
            String str = new String("str" + i);
            list.add(str);
        }

        System.out.println("结束测试内存监控线程测试");
        System.exit(0);
    }

    private static void start() {
        Thread thread = new Thread(new MemoryWatcherDemo());
        thread.setPriority(Thread.MAX_PRIORITY);
        // 设置后台运行
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        long memoryUsed = getMemoryUsed();
        showMemoryUsed(memoryUsed);

        while (true) {
            long newMemoryUsed = getMemoryUsed();
            if (memoryUsed != newMemoryUsed) {
                memoryUsed = newMemoryUsed;
                showMemoryUsed(memoryUsed);
            }
        }
    }

    private void showMemoryUsed(long memoryUsed) {
        System.out.println("内存使用:" + memoryUsed + " MB");
    }

    private long getMemoryUsed() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
    }
}
