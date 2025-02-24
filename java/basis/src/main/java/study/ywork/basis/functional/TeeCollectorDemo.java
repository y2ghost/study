package study.ywork.basis.functional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeeCollectorDemo {
    public static void main(String[] args) {
        var result =
                Stream.of("macOS X", "Linux", "Windows XP", "OpenBSD Unix", "Windows 10")
                        .collect(Collectors.teeing(
                                Collectors.filtering(n -> n.toLowerCase().endsWith("x"), Collectors.toList()),
                                Collectors.filtering(n -> n.contains("w"), Collectors.toList()),
                                List::of
                        ));

        System.out.println(result);
    }
}
