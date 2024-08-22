package algs.test;

import algs.collection.Stack;

public class StackTest {
    public static void main(String[] args) {
        Stack stack = new Stack(10);
        stack.push(20);
        stack.push(40);
        stack.push(60);
        stack.push(80);

        while (!stack.isEmpty()) {
            long value = stack.pop();
            System.out.print(value);
            System.out.print(" ");
        }

        System.out.println("");
    }
}