package study.ywork.multi.thread.pool;

public class MyTask implements Runnable {
    private static final long START;
    private final String name;
    private long repeatCount;
    private long taskStart;

    static {
        START = System.currentTimeMillis();
    }

    public MyTask(String s) {
        this.name = s;
    }

    @Override
    public void run() {
        taskStart = System.currentTimeMillis();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        repeatCount++;
        printTaskInfo();
    }

    private void printTaskInfo() {
        StringBuilder builder = new StringBuilder(" ").append(name)
            .append(" - Repeat count: ")
            .append(repeatCount)
            .append(" - Exec At: ")
            .append(taskStart - START)
            .append(" - Task duration: " + (System.currentTimeMillis() - taskStart));

        System.out.println(builder);
    }

    public String getName() {
        return name;
    }
}
