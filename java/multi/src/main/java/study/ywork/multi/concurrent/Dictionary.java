package study.ywork.multi.concurrent;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* 测试读写锁功能 */
public class Dictionary {
    public static void main(String[] args) {
        final String[] words = {
            "hypocalcemia",
            "prolixity",
            "assiduous",
            "indefatigable",
            "castellan",
        };
        final String[] definitions = {
            "a deficiency of calcium in the blood",
            "unduly prolonged or drawn out",
            "showing great care, attention, and effort",
            "able to work or continue for a lengthy time without tiring",
            "the govenor or warden of a castle or fort",
        };
        final Map<String, String> dictionary = new HashMap<>();
        ReadWriteLock rwl = new ReentrantReadWriteLock(true);
        final Lock rlock = rwl.readLock();
        final Lock wlock = rwl.writeLock();

        Runnable writer = () -> {
            for (int i = 0; i < words.length; i++) {
                wlock.lock();
                try {
                    dictionary.put(words[i], definitions[i]);
                    System.out.println("writer storing " + words[i] + " entry");
                } finally {
                    wlock.unlock();
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    System.err.println("writer " + "interrupted");
                    System.err.println(ex);
                    Thread.currentThread().interrupt();
                }
            }
        };

        ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(writer);

        Runnable reader = () -> {
            while (true) {
                rlock.lock();
                try {
                    int i = (int) (new Random().nextInt() * words.length);
                    System.out.println("reader accessing " + words[i] + ": " + dictionary.get(words[i]) + " entry");
                } finally {
                    rlock.unlock();
                }
            }
        };

        es = Executors.newFixedThreadPool(1);
        es.submit(reader);
    }
}