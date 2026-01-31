package study.ywork.multi.mutex;

public class ThreadID {
    // 下一个分配的线程ID
    private static volatile int nextID = 0;
    private static ThreadLocalID threadlocalID = new ThreadLocalID();

    public static int get() {
        return threadlocalID.get();
    }

    public static void reset() {
        nextID = 0;
    }

    private static class ThreadLocalID extends ThreadLocal<Integer> {
        @Override
        protected synchronized Integer initialValue() {
            return nextID++;
        }
    }

    private ThreadID() {
        // 禁止初始化
    }
}
