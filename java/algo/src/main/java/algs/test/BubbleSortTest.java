package algs.test;

import algs.collection.BubbleSort;

public class BubbleSortTest {
    public static void main(String[] args) {
        BubbleSort bubble = new BubbleSort(100);
        bubble.insert(77);
        bubble.insert(99);
        bubble.insert(44);
        bubble.insert(55);
        bubble.insert(22);
        bubble.insert(88);
        bubble.insert(11);
        bubble.insert(00);
        bubble.insert(66);
        bubble.insert(33);
        System.out.println(bubble);
        bubble.sort();
        System.out.println(bubble);
    }
}