package algs.collection;

public class PriorityQueue {
    private final long[] array;
    private final int size;
    private int count;

    public PriorityQueue(int size) {
        this.size = size;
        array = new long[size];
        count = 0;
    }

    public void insert(long elm) {
        if (0 == count) {
            array[count++] = elm;
            return;
        }

        int i = 0;
        for (i = count - 1; i >= 0; --i) {
            if (elm <= array[i]) {
                break;
            }

            array[i + 1] = array[i];
        }

        array[i + 1] = elm;
        count++;
    }

    public long remove() {
        return array[--count];
    }

    public boolean isEmpty() {
        return 0 == count;
    }

    public boolean isFull() {
        return size == count;
    }

    public long peek() {
        return array[count - 1];
    }
}