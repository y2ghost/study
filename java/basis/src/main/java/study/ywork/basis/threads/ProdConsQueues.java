package study.ywork.basis.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ProdConsQueues {
    protected volatile boolean done = false;
    protected ExecutorService threadPool = Executors.newCachedThreadPool();

    class Producer implements Runnable {
        protected BlockingQueue<Object> queue;

        Producer(BlockingQueue<Object> theQueue) {
            this.queue = theQueue;
        }

        public void run() {
            try {
                while (!done) {
                    Object justProduced = getRequestFromNetwork();
                    queue.put(justProduced);
                    System.out.println("Produced 1 object; List size now " + queue.size());
                }
            } catch (InterruptedException ex) {
                System.out.println("Producer INTERRUPTED");
                Thread.currentThread().interrupt();
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

    class Consumer implements Runnable {
        protected BlockingQueue<Object> queue;

        Consumer(BlockingQueue<Object> theQueue) {
            this.queue = theQueue;
        }

        public void run() {
            try {
                while (true) {
                    Object obj = queue.take();
                    int len = queue.size();
                    System.out.println("List size now " + len);
                    process(obj);
                    if (done) {
                        return;
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("CONSUMER INTERRUPTED");
                Thread.currentThread().interrupt();
            }
        }

        void process(Object obj) {
            System.out.println("Consuming object " + obj);
        }
    }

    ProdConsQueues(int nP, int nC) {
        BlockingQueue<Object> myQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < nP; i++)
            threadPool.submit(new Producer(myQueue));
        for (int i = 0; i < nC; i++)
            threadPool.submit(new Consumer(myQueue));
    }

    public static void main(String[] args) throws InterruptedException {
        int numProducers = 4;
        int numConsumers = 3;
        ProdConsQueues pc = new ProdConsQueues(numProducers, numConsumers);
        Thread.sleep(10 * 1000L);
        // 处理业务 ...
        pc.done = true;
        pc.threadPool.shutdown();
    }
}
