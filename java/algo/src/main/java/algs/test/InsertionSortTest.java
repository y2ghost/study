package algs.test;

import algs.collection.InsertionSort;

public class InsertionSortTest {
    public static void main(String[] args) {
        InsertionSort insertion = new InsertionSort(100);
        insertion.insert(77);
        insertion.insert(99);
        insertion.insert(44);
        insertion.insert(55);
        insertion.insert(22);
        insertion.insert(88);
        insertion.insert(11);
        insertion.insert(00);
        insertion.insert(66);
        insertion.insert(33);
        System.out.println(insertion);
        insertion.sort();
        System.out.println(insertion);
    }
}