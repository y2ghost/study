package study.ywork.multi.mutex;

import java.util.Random;

// 模拟哲学家就餐，饿了需要使用左右手的筷子
public class Philosopher extends Thread {
    private int id;
    private static Random random = new Random();
    private Chopstick left;
    private Chopstick right;

    public Philosopher(int myID, Chopstick left, Chopstick right) {
        this.id = myID;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        
        while (true) {
            try {
                sleep(random.nextInt(1000));
                sleep(100);
                System.out.println(id + "号哲学家饿了");

                if (0 == id % 2) {
                    left.get();
                    right.get();
                } else {
                    right.get();
                    left.get();
                }

                System.out.println(id + "号哲学家吃饱了");
                left.put();
                right.put();
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
