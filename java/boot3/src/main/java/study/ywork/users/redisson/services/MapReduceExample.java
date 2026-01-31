package study.ywork.users.redisson.services;

import org.redisson.Redisson;
import org.redisson.api.RExecutorService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.redisson.api.mapreduce.RCollator;
import org.redisson.api.mapreduce.RCollector;
import org.redisson.api.mapreduce.RMapReduce;
import org.redisson.api.mapreduce.RMapper;
import org.redisson.api.mapreduce.RReducer;

import java.util.Iterator;
import java.util.Map;

public class MapReduceExample {
    public static class WordMapper implements RMapper<String, String, String, Integer> {

        @Override
        public void map(String key, String value, RCollector<String, Integer> collector) {
            String[] words = value.split("[^a-zA-Z]");
            for (String word : words) {
                collector.emit(word, 1);
            }
        }
    }

    public static class WordReducer implements RReducer<String, Integer> {
        @Override
        public Integer reduce(String reducedKey, Iterator<Integer> iter) {
            int sum = 0;
            while (iter.hasNext()) {
                Integer i = iter.next();
                sum += i;
            }
            return sum;
        }

    }

    public static class WordCollator implements RCollator<String, Integer, Integer> {
        @Override
        public Integer collate(Map<String, Integer> resultMap) {
            int result = 0;
            for (Integer count : resultMap.values()) {
                result += count;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        redisson.getExecutorService(RExecutorService.MAPREDUCE_NAME).registerWorkers(WorkerOptions.defaults().workers(3));
        RMap<String, String> map = redisson.getMap("myMapReduce");

        map.put("1", "Alice was beginning to get very tired");
        map.put("2", "of sitting by her sister on the bank and");
        map.put("3", "of having nothing to do once or twice she");
        map.put("4", "had peeped into the book her sister was reading");
        map.put("5", "but it had no pictures or conversations in it");
        map.put("6", "and what is the use of a book");
        map.put("7", "thought Alice without pictures or conversation");

        RMapReduce<String, String, String, Integer> mapReduce = map
                .<String, Integer>mapReduce()
                .mapper(new WordMapper())
                .reducer(new WordReducer());

        Integer count = mapReduce.execute(new WordCollator());
        System.out.println("Count " + count);

        Map<String, Integer> resultMap = mapReduce.execute();
        System.out.println("Result " + resultMap);
        redisson.shutdown();
    }
}
