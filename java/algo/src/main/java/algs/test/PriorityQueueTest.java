package algs.test;

import algs.collection.PriorityQueue;

public class PriorityQueueTest {
    public static void main(String[] args) {
        PriorityQueue queue = new PriorityQueue(5);
        queue.insert(30);
        queue.insert(50);
        queue.insert(10);
        queue.insert(40);
        queue.insert(20);

        while (!queue.isEmpty()) {
            long item = queue.remove();
            System.out.print(item + " ");
        }

        System.out.println("");
    }
}