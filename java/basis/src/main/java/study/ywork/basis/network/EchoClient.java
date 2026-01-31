package study.ywork.basis.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {
    public static void main(String[] argv) {
        EchoClient c = new EchoClient();
        c.converse(argv.length == 1 ? argv[0] : "localhost");
    }

    protected void converse(String hostname) {
        try (Socket sock = new Socket(hostname, 7)) {
            BufferedReader stdin = new BufferedReader(
                    new InputStreamReader(System.in));
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(sock.getInputStream(), StandardCharsets.ISO_8859_1));
            PrintWriter os = new PrintWriter(
                    new OutputStreamWriter(
                            sock.getOutputStream(), StandardCharsets.ISO_8859_1), true);

            do {
                System.out.print(">> ");
                String line = stdin.readLine();

                if (null == line) {
                    break;
                }

                os.print(line + "\r\n");
                os.flush();
                String reply = is.readLine();
                System.out.print("<< ");
                System.out.println(reply);
            } while (true);
        } catch (IOException e) {
            System.err.println(e);
        }
        // 不做事
    }
}
