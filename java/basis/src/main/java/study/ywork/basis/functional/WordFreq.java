package study.ywork.basis.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFreq {
    public static void main(String[] args) throws IOException {
        String fileName = args.length == 0 ? "WordFreq.java" : args[0];
        try (Stream<String> lines = Files.lines(Path.of(fileName))) {
            Map<String, Long> map = lines
                    .filter(line -> !line.isEmpty())
                    .flatMap(s -> Stream.of(s.split(" ")))
                    .collect(Collectors.groupingBy(
                            String::toLowerCase, Collectors.counting()));
            map.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue()
                            .reversed())
                    .limit(20)
                    .map(entry -> "%4d %s".formatted(entry.getValue(), entry.getKey()))
                    .forEach(System.out::println);
        }
    }
}
