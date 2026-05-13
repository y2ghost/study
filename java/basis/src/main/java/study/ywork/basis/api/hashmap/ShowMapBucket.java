package study.ywork.basis.api.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ShowMapBucket {
    public static void main(String[] args) throws Exception {
        Map<Person, Integer> personSalaryMap = new HashMap<>();
        for (int i = 20; i <= 30; i++) {
            Person person = new Person("LTYY", i);
            personSalaryMap.put(person, ThreadLocalRandom.current().nextInt(100, 1000));
        }

        printHashMapBuckets(personSalaryMap);
    }

    private static void printHashMapBuckets(Map<Person, Integer> personSalaryMap)
            throws NoSuchFieldException, IllegalAccessException {
        // HashMap#table 字段就是bucket数组
        Field bucketArrayField = HashMap.class.getDeclaredField("table");
        bucketArrayField.trySetAccessible();
        Object[] buckets = (Object[]) bucketArrayField.get(personSalaryMap);
        System.out.println("Number of buckets: " + buckets.length);

        for (int i = 0; i < buckets.length; i++) {
            Object node = buckets[i];
            if (node == null) {
                continue;
            }

            System.out.printf("%n-- bucket index: %s --%n", i);
            print(1, "Node: " + node);
            printNode(node, 1);
        }
    }

    private static void printNode(Object node, int level) throws IllegalAccessException {
        for (Field declaredField : node.getClass().getDeclaredFields()) {
            declaredField.trySetAccessible();
            String fieldName = declaredField.getName();
            Object value = declaredField.get(node);

            if (fieldName.equals("next")) {
                if (value == null) {
                    continue;
                }

                print(level, "Next Node:" + value);
                printNode(value, level + 1);

            } else {
                print(level, fieldName + " = " + value);
            }
        }
    }

    private static void print(int level, String string) {
        int padSize = level * 4 - 3;
        String format = "%" + padSize + "s|- %s%n";
        System.out.printf(format, "", string);
    }
}
