package algs.collection;

public class Queue {
    private final int size;
    private final long[] array;
    private int front;
    private int rear;
    private int count;

    public Queue(int size) {
        this.size = size;
        array = new long[size];
        front = 0;
        rear = -1;
        count = 0;
    }

    public void insert(long elm) {
        if (size - 1 == rear) {
            rear = -1;
        }

        array[++rear] = elm;
        count++;
    }

    public long remove() {
        long elm = array[front++];
        if (size == front) {
            front = 0;
        }

        count--;
        return elm;
    }

    public long peekFront() {
        return array[front];
    }

    public boolean isEmpty() {
        return 0 == count;
    }

    public boolean isFull() {
        return count == size;
    }

    public int size() {
        return count;
    }
}