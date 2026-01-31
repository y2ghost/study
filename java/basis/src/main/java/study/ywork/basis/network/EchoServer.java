package study.ywork.basis.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoServer {
    protected ServerSocket sock;
    public static final int ECHO_PORT = 7;
    protected boolean debug = true;

    public static void main(String[] args) {
        int p = ECHO_PORT;
        if (args.length == 1) {
            try {
                p = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage: EchoServer [port#]");
                System.exit(1);
            }
        }
        new EchoServer(p).handle();
    }

    public EchoServer(int port) {
        try {
            sock = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("I/O error in setup");
            System.err.println(e);
            System.exit(1);
        }
    }

    protected void handle() {
        while (true) {
            try {
                System.out.println("Waiting for client...");
                Socket ios = sock.accept();
                if (null == ios) {
                    break;
                }

                System.err.println("Accepted from " +
                        ios.getInetAddress().getHostName());
                try (BufferedReader is = new BufferedReader(
                        new InputStreamReader(ios.getInputStream(), StandardCharsets.ISO_8859_1));
                     PrintWriter os = new PrintWriter(
                             new OutputStreamWriter(ios.getOutputStream(), StandardCharsets.ISO_8859_1),
                             true);) {
                    while (true) {
                        String echoLine = is.readLine();
                        if (null == echoLine) {
                            break;
                        }

                        System.err.println("Read " + echoLine);
                        os.print(echoLine + "\r\n");
                        os.flush();
                        System.err.println("Wrote " + echoLine);
                    }
                    System.err.println("All done!");
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}

