package study.ywork.vertx.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncEcho {
    private static final boolean INFINITE_LOOP = true;
    private static final Logger logger = LoggerFactory.getLogger(SyncEcho.class);

    public static void main(String[] args) throws Throwable {
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(3000));
            while (INFINITE_LOOP) {
                // 每次客户端新连接，都会使用新建一个线程进行处理
                Socket socket = server.accept();
                new Thread(clientHandler(socket)).start();
            }
        }
    }

    private static Runnable clientHandler(Socket socket) {
        return () -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                String line = "";
                while (!"/quit".equals(line)) {
                    // 读取数据可能存在阻塞情况
                    line = reader.readLine();
                    logger.info("~ {}", line);
                    // 发送数据可能存在阻塞情况
                    writer.write(line + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        };
    }
}
