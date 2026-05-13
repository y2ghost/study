package study.ywork.multi.mutex;

// 模拟筷子对象
public class Chopstick {
    private boolean taken;
    private int id;

    public Chopstick(int myID) {
        this.id = myID;
    }

    // 筷子被使用的情况，其他哲学家必须等待筷子使用完毕
    public synchronized void get() throws InterruptedException {
        while (taken) {
            wait();
        }

        taken = true;
    }

    // 筷子使用完毕放回，发出通知
    public synchronized void put() {
        taken = false;
        notify();
    }

    @Override
    public String toString() {
        return "Chopstick [taken=" + taken + ", id=" + id + "]";
    }
}
