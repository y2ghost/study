package algs.test;

import algs.collection.SelectionSort;

public class SelectionSortTest {
    public static void main(String[] args) {
        SelectionSort selection = new SelectionSort(100);
        selection.insert(77);
        selection.insert(99);
        selection.insert(44);
        selection.insert(55);
        selection.insert(22);
        selection.insert(88);
        selection.insert(11);
        selection.insert(00);
        selection.insert(66);
        selection.insert(33);
        System.out.println(selection);
        selection.sort();
        System.out.println(selection);
    }
}