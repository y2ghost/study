package study.ywork.basis.functional;

import java.util.stream.Stream;

public class StreamGenerate {
    public static void main(String[] args) {
        Stream.generate(StreamGenerate::produceValue).limit(10).forEach(System.out::println);
    }

    static String[] data = {"Fluffy", "Peter", "Roger"};
    static int index;

    static String produceValue() {
        return data[index++ % data.length];
    }
}
