package study.ywork.basis.functional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class SimpleCollectorsGroupingBy {
    static Customer chip = new Customer("Chip");
    static Customer dale = new Customer("Dale");

    static List<Transaction> transactionList = List.of(
            new Transaction(chip, LocalDate.now(), 2.34),
            new Transaction(dale, LocalDate.now().minusDays(1), 3.21),
            new Transaction(chip, LocalDate.now().minusDays(2), 6.50),
            new Transaction(dale, LocalDate.now().minusDays(3), 0.50)
    );

    public static void main(String[] args) {
        process(transactionList);
    }

    static void process(List<Transaction> transactionList) {
        Map<Customer, List<Transaction>> map = transactionList.stream().collect(groupingBy(Transaction::customer));
        map.forEach((k, v) -> System.out.printf("Customer %s, Transaction %s%n", k.name(), v));
    }

    record Customer(String name) {
        // 不做事儿
    }

    record Transaction(Customer customer, LocalDate when, double amount) {
        public String toString() {
            return "Customer %s, Date %s, transaction amount %.2f".formatted(customer.name(), when, amount);
        }
    }
}
