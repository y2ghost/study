package study.ywork.multi.mutex;

// 模拟经典的哲学家就餐互斥问题
public class PhilosopherDining {
    public static void main(String[] args) {
        // 五只筷子，五位哲学家
        Chopstick[] chopsticks = new Chopstick[5];
        for (int i = 0; i < 5; i++) {
            chopsticks[i] = new Chopstick(i);
        }

        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % 5]);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }
    }
}
