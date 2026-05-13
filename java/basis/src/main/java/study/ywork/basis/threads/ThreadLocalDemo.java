package study.ywork.basis.threads;

public class ThreadLocalDemo extends Thread {
    private static int clientNum = 0;

    ThreadLocalDemo(String name) {
        super.setName(name);
    }

    private static final ThreadLocal<Client> myClient = ThreadLocal.withInitial(() ->
            new Client(++clientNum)
    );

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() +
                " sees client " + myClient.get());
    }

    public static void main(String[] args) {
        new ThreadLocalDemo("demo 1").start();
        new ThreadLocalDemo("demo 2").start();
        Thread.yield();
        System.out.println("Main program sees client " + myClient.get());
        myClient.remove();
    }


    private record Client(int clientNum) {
    }
}
