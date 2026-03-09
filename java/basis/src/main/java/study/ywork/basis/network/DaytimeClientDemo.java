package study.ywork.basis.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DaytimeClientDemo {
    public static void main(String[] args) throws IOException {
        String hostname = args.length > 0 ? args[0] : "time.nist.gov";
        try (Socket socket = new Socket(hostname, 13)) {
            socket.setSoTimeout(15000);
            InputStream in = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.US_ASCII);

            for (int c = reader.read(); -1 != c; c = reader.read()) {
                time.append((char) c);
            }

            System.out.println(time);
        }
    }
}