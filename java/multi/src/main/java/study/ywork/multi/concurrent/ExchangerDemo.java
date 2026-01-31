package study.ywork.multi.concurrent;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/* 测试线程交换数据 */
public class ExchangerDemo {
    private static final Exchanger<DataBuffer> EXCHANGER = new Exchanger<>();
    private static final DataBuffer EMPTY_BUFFER = new DataBuffer();
    private static final DataBuffer FULL_BUFFER = new DataBuffer("I");

    public static void main(String[] args) {
        class FillingLoop implements Runnable {
            int count = 0;

            @Override
            public void run() {
                DataBuffer currentBuffer = EMPTY_BUFFER;
                try {
                    while (true) {
                        addToBuffer(currentBuffer);
                        if (currentBuffer.isFull()) {
                            System.out.println("filling thread wants to exchange");
                            currentBuffer = EXCHANGER.exchange(currentBuffer);
                            System.out.println("filling thread receives exchange");
                        }
                    }
                } catch (InterruptedException ie) {
                    System.out.println("filling thread interrupted");
                    Thread.currentThread().interrupt();
                }
            }

            void addToBuffer(DataBuffer buffer) {
                String item = "NI" + count++;
                System.out.println("Adding: " + item);
                buffer.add(item);
            }
        }

        class EmptyingLoop implements Runnable {
            @Override
            public void run() {
                DataBuffer currentBuffer = FULL_BUFFER;
                try {
                    while (true) {
                        takeFromBuffer(currentBuffer);
                        if (currentBuffer.isEmpty()) {
                            System.out.println("emptying thread wants to " + "exchange");
                            currentBuffer = EXCHANGER.exchange(currentBuffer);
                            System.out.println("emptying thread receives " + "exchange");
                        }
                    }
                } catch (InterruptedException ie) {
                    System.out.println("emptying thread interrupted");
                    Thread.currentThread().interrupt();
                }
            }

            void takeFromBuffer(DataBuffer buffer) {
                System.out.println("taking: " + buffer.remove());
            }
        }

        new Thread(new EmptyingLoop()).start();
        new Thread(new FillingLoop()).start();
    }
}

class DataBuffer {
    private static final int MAXITEMS = 10;
    private final List<String> items = new ArrayList<>();

    DataBuffer() {
    }

    DataBuffer(String prefix) {
        for (int i = 0; i < MAXITEMS; i++) {
            String item = prefix + i;
            System.out.printf("Adding %s%n", item);
            items.add(item);
        }
    }

    synchronized void add(String s) {
        if (!isFull()) {
            items.add(s);
        }
    }

    synchronized boolean isEmpty() {
        return items.isEmpty();
    }

    synchronized boolean isFull() {
        return items.size() == MAXITEMS;
    }

    synchronized String remove() {
        if (!isEmpty()) {
            return items.remove(0);
        }

        return null;
    }
}