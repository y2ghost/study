package study.ywork.basis.threads;

import java.util.LinkedList;

public class ProdConsThreadAPI {
    protected final LinkedList<Object> list = new LinkedList<>();
    protected static final int MAX = 10;
    protected boolean done = false;

    class Producer extends Thread {
        @Override
        public void run() {
            while (!done) {
                Object justProduced = getRequestFromNetwork();
                synchronized (list) {
                    while (list.size() == MAX) {
                        try {
                            System.out.println("Producer WAITING");
                            list.wait();
                        } catch (InterruptedException ex) {
                            System.out.println("Producer INTERRUPTED");
                            Thread.currentThread().interrupt();
                        }
                    }

                    list.addFirst(justProduced);
                    list.notifyAll();
                    System.out.println("Produced 1; List size now " + list.size());
                }
            }
        }

        Object getRequestFromNetwork() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.out.println("Producer Read INTERRUPTED");
                Thread.currentThread().interrupt();
            }
            return new Object();
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                Object obj;
                synchronized (list) {
                    while (list.isEmpty()) {
                        try {
                            System.out.println("CONSUMER WAITING");
                            list.wait();
                        } catch (InterruptedException ex) {
                            System.out.println("CONSUMER INTERRUPTED");
                            Thread.currentThread().interrupt();
                        }
                    }

                    obj = list.removeLast();
                    list.notifyAll();
                    int len = list.size();
                    System.out.println("List size now " + len);

                    if (done) {
                        break;
                    }
                }

                process(obj);
            }
        }

        void process(Object obj) {
            System.out.println("Consuming object " + obj);
        }
    }

    ProdConsThreadAPI(int nP, int nC) {
        for (int i = 0; i < nP; i++) {
            new Producer().start();
        }

        for (int i = 0; i < nC; i++) {
            new Consumer().start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int numProducers = 4;
        int numConsumers = 3;
        ProdConsThreadAPI pc = new ProdConsThreadAPI(numProducers, numConsumers);
        Thread.sleep(10 * 1000L);

        synchronized (pc.list) {
            pc.done = true;
            pc.list.notifyAll();
        }
    }
}
