package study.ywork.basis.functional;

import java.util.stream.Stream;

public class StreamBuild {
    public static void main(String[] args) {
        Stream.Builder<String> sb = Stream.builder();
        for (String str : data)
            sb.add(str);
        sb.build().forEach(System.out::println);
    }

    static String[] data = {"Fluffy", "Peter", "Roger"};
}
