package study.ywork.basis.api.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapDemo {
    public static void main(String[] args) {
        Map<Integer, List<Person>> bucketMap = new HashMap<>();
        for (int i = 20; i <= 30; i++) {
            Person person = new Person("LTYY", i);
            int hashCode = person.hashCode();
            List<Person> persons = bucketMap.computeIfAbsent(hashCode, k -> new ArrayList<>());
            persons.add(person);
        }

        bucketMap.forEach((k, v) -> {
            System.out.println("hashcode: " + k);
            v.forEach(x -> System.out.println("    " + x));
        });
    }
}
