package algs.collection;

public class BubbleSort {
    private final long[] array;
    private int count;

    public BubbleSort(int max) {
        array = new long[max];
        count = 0;
    }

    public void insert(long value) {
        array[count++] = value;
    }

    public void sort() {
        for (int out = count - 1; out > 1; out--) {
            for (int in = 0; in < out; in++) {
                if (array[in] > array[in + 1]) {
                    swap(in, in + 1);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            builder.append(array[i]).append(" ");
        }

        builder.append(" ");
        return builder.toString();
    }

    private void swap(int elm1, int elm2) {
        long temp = array[elm1];
        array[elm1] = array[elm2];
        array[elm2] = temp;
    }
}