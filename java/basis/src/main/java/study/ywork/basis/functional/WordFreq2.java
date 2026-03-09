package study.ywork.basis.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFreq2 {
    public static void main(String[] args) throws IOException {
        String fileName = args.length == 0 ? "WordFreq2.java" : args[0];
        try (var lines = Files.lines(Path.of(fileName))) {
            lines.filter(line -> !line.isEmpty())
                    .flatMap(s -> Stream.of(s.split(" ")))
                    .collect(Collectors.groupingBy(
                            String::toLowerCase, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue()
                            .reversed())
                    .limit(20)
                    .map(entry -> "%4d %s".formatted(entry.getValue(), entry.getKey()))
                    .forEach(System.out::println);
        }
    }
}

