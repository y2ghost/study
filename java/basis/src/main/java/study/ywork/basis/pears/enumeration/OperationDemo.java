package study.ywork.basis.pears.enumeration;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toMap;

public class OperationDemo {
    private static final Map<String, Operation> stringToEnum = Stream.of(Operation.values())
        .collect(toMap(Object::toString, e -> e));

    public static Optional<Operation> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }

    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);

        for (Operation op : Operation.values()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }

        System.out.println(fromString("/"));
    }
}
