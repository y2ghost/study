package algs.collection;

public class Stack {
    private final long[] array;
    private final int size;
    private int top;

    public Stack(int size) {
        array = new long[size];
        this.size = size;
        top = -1;
    }

    public void push(long elm) {
        array[++top] = elm;
    }

    public long pop() {
        return array[top--];
    }

    public long peek() {
        return array[top];
    }

    public boolean isEmpty() {
        return (-1) == top;
    }

    public boolean isFull() {
        return (size - 1) == top;
    }
}