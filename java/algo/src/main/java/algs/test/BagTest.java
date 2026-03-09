package algs.test;

import algs.collection.Bag;

class BagTest {
    public static void main(String[] args) {
        Bag<String> bag = new Bag<>();
        bag.add("12");
        bag.add("qq");
        bag.add("gg");
        System.out.println(bag);
    }
}