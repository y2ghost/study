package study.ywork.basis.threads;

public class ThreadLocalDemo3 extends Thread {
    private static int clientNum = 0;
    private static final ThreadLocal<Client> myClient = new ThreadLocal<>();

    ThreadLocalDemo3(String name) {
        super.setName(name);
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() +
                " sees client " + getClient());
    }

    synchronized Client getClient() {
        if (myClient.get() == null) {
            myClient.set(new Client(++clientNum));
        }
        return myClient.get();
    }

    public static void main(String[] args) {
        new ThreadLocalDemo3("Demo 1").start();
        new ThreadLocalDemo3("Demo 2").start();
        Thread.yield();
        System.out.println("Main program sees client " + myClient.get());
        myClient.remove();
    }

    private record Client(int clientNum) {
    }
}
