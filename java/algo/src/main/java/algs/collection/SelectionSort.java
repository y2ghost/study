package algs.collection;

public class SelectionSort {
    private final long[] array;
    private int count;

    public SelectionSort(int max) {
        array = new long[max];
        count = 0;
    }

    public void insert(long value) {
        array[count++] = value;
    }

    public void sort() {
        int min = 0;
        for (int out = 0; out < count - 1; ++out) {
            min = out;
            for (int in = out + 1; in < count; in++) {
                if (array[in] < array[min]) {
                    min = in;
                }
            }

            swap(out, min);
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