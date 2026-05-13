package study.ywork.basis.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalDemo2 extends Thread {
    private static final AtomicInteger clientNum = new AtomicInteger(0);
    private static final ThreadLocal<Client> myClient = ThreadLocal.withInitial(() -> new Client(clientNum.incrementAndGet()));

    ThreadLocalDemo2(String name) {
        super.setName(name);
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() +
                " sees client " + myClient.get());
    }

    public static void main(String[] args) {
        new ThreadLocalDemo2("Demo 1").start();
        new ThreadLocalDemo2("Demo 2").start();
        Thread.yield();
        System.out.println("Main program sees client " + myClient.get());
        myClient.remove();
    }

    private record Client(int clientNum) {
    }
}
