package study.ywork.multi.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/* 同步屏障功能测试 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        float[][] matrix = new float[3][3];
        int counter = 0;

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] = counter++;
            }
        }

        dump(matrix);
        System.out.println();
        Solver solver = new Solver(matrix);
        solver.start();
        dump(matrix);
    }

    static void dump(float[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }

            System.out.println();
        }
    }
}

class Solver {
    private static final Object LOCKER = new Object();
    final int n;
    final float[][] data;
    final CyclicBarrier barrier;

    class Worker implements Runnable {
        int myRow;
        boolean done = false;

        Worker(int row) {
            myRow = row;
        }

        boolean done() {
            return done;
        }

        void processRow(int myRow) {
            System.out.println("Processing row: " + myRow);
            for (int i = 0; i < n; i++) {
                data[myRow][i] *= 10;
            }

            done = true;
        }

        @Override
        public void run() {
            while (!done()) {
                processRow(myRow);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public Solver(float[][] matrix) {
        data = matrix;
        n = matrix.length;
        barrier = new CyclicBarrier(n, this::mergeRows);
    }

    public void start() {
        for (int i = 0; i < n; ++i) {
            new Thread(new Worker(i)).start();
        }

        waitUntilDone();
    }

    private void mergeRows() {
        System.out.println("merging");
        synchronized (LOCKER) {
            LOCKER.notifyAll();
        }
    }

    private void waitUntilDone() {
        synchronized (LOCKER) {
            try {
                System.out.println("main thread waiting");
                LOCKER.wait();
                System.out.println("main thread notified");
            } catch (InterruptedException ie) {
                System.out.println("main thread interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
}