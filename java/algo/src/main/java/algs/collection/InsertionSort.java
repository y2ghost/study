package algs.collection;

public class InsertionSort {
    private final long[] array;
    private int count;

    public InsertionSort(int max) {
        array = new long[max];
        count = 0;
    }

    public void insert(long value) {
        array[count++] = value;
    }

    public void sort() {
        for (int out = 1; out < count; ++out) {
            long temp = array[out];
            int in = out;

            while (in > 0 && array[in - 1] >= temp) {
                array[in] = array[in - 1];
                in--;
            }

            array[in] = temp;
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
}