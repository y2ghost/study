package study.ywork.basis.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ParallelStreamDemo {
    public static void main(String[] args) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of("employee-data.txt"))) {
            long max = lines.parallel()
                    .map(s -> Employee.toEmployee(s))
                    .mapToLong(Employee::getPersonnelNumber)
                    .max()
                    .getAsLong();
            System.out.println("Highest personnel number is " + max);
        }
    }

    private static class Employee {
        private Long personnelNumber;

        public Long getPersonnelNumber() {
            return personnelNumber;
        }

        public void setPersonnelNumber(Long personnelNumber) {
            this.personnelNumber = personnelNumber;
        }

        public static Employee toEmployee(String info) {
            Employee employee = new Employee();
            employee.setPersonnelNumber(Long.valueOf(info.hashCode()));
            return employee;
        }
    }
}
