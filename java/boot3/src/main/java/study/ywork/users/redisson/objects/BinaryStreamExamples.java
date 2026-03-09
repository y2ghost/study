package study.ywork.users.redisson.objects;

import org.redisson.Redisson;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BinaryStreamExamples {
    public static void main(String[] args) throws IOException {
        // 默认连接 127.0.0.1:6379
        RedissonClient redisson = Redisson.create();

        RBinaryStream stream = redisson.getBinaryStream("myStream");

        byte[] values = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        stream.setIfAbsent(values);
        stream.set(values);

        InputStream is = stream.getInputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            System.out.println(ch);
        }

        OutputStream os = stream.getOutputStream();
        for (byte c : values) {
            os.write(c);
        }

        redisson.shutdown();
    }
}
