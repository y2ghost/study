package study.ywork.users.redisson.collections;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.redisson.api.options.MapOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapReadWriteThroughExamples {
    public static void main(String[] args) {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();
        MapOptions<String, String> options;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "t123", "w1w2w3w4")) {
            MyMapWriter mapWriter = new MyMapWriter(conn);
            MyMapLoader mapLoader = new MyMapLoader(conn);
            options = MapOptions.<String, String>name("wtMap")
                    .writer(mapWriter)
                    .loader(mapLoader);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        RMap<String, String> map = redisson.getMap(options);
        map.put("1", "Willy");
        map.put("2", "Andrea");
        map.put("3", "Bob");

        String name1 = map.get("1");
        System.out.println("name1 " + name1);

        String name2 = map.get("2");
        System.out.println("name2 " + name2);

        String name3 = map.get("3");
        System.out.println("name3 " + name3);

        redisson.shutdown();
    }

    private static class MyMapWriter implements MapWriter<String, String> {
        private final Connection conn;

        public MyMapWriter(Connection conn) {
            this.conn = conn;
        }

        @Override
        public void write(Map<String, String> map) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO student (id, name) values (?, ?)")) {
                for (Entry<String, String> entry : map.entrySet()) {
                    preparedStatement.setString(1, entry.getKey());
                    preparedStatement.setString(2, entry.getValue());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException ex) {
                throw new IllegalStateException(ex);
            }

        }

        @Override
        public void delete(Collection<String> keys) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM student where id = ?")) {
                for (String key : keys) {
                    preparedStatement.setString(1, key);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    private static class MyMapLoader implements MapLoader<String, String> {
        private final Connection conn;

        public MyMapLoader(Connection conn) {
            this.conn = conn;
        }

        @Override
        public Iterable<String> loadAllKeys() {
            List<String> list = new ArrayList<>();
            try (Statement statement = conn.createStatement()) {
                ResultSet result = statement.executeQuery("SELECT id FROM student");
                while (result.next()) {
                    list.add(result.getString(1));
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }

            return list;
        }

        @Override
        public String load(String key) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT name FROM student where id = ?")) {
                preparedStatement.setString(1, key);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    return result.getString(1);
                }
                return null;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
