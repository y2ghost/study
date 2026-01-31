package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RMap<String, Integer> map = redisson.getMap("myMap");
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        boolean contains = map.containsKey("a");
        System.out.println("contains " + contains);

        Integer value = map.get("c");
        System.out.println("value " + value);
        
        Integer valueSize = map.valueSize("c");
        System.out.println("valueSize " + valueSize);

        Set<String> keys = new HashSet<>();
        keys.add("a");
        keys.add("b");
        keys.add("c");
        Map<String, Integer> mapSlice = map.getAll(keys);
        System.out.println("mapSlice " + mapSlice);

        Set<String> allKeys = map.readAllKeySet();
        System.out.println("allKeys " + allKeys);

        Collection<Integer> allValues = map.readAllValues();
        System.out.println("allValues " + allValues);

        Set<Entry<String, Integer>> allEntries = map.readAllEntrySet();
        System.out.println("allEntries " + allEntries);

        boolean isNewKey = map.fastPut("a", 100);
        System.out.println("isNewKey " + isNewKey);

        boolean isNewKeyPut = map.fastPutIfAbsent("d", 33);
        System.out.println("isNewKeyPut " + isNewKeyPut);

        long removedAmount = map.fastRemove("b");
        System.out.println("removedAmount " + removedAmount);

        redisson.shutdown();
    }
}
