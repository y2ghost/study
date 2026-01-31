package study.ywork.basis.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClientOneLine {
    String message = "Hello across the net";

    public static void main(String[] argv) {
        if (argv.length == 0)
            new EchoClientOneLine().converse("localhost");
        else
            new EchoClientOneLine().converse(argv[0]);
    }

    protected void converse(String hostName) {
        try (Socket sock = new Socket(hostName, 7);) {
            BufferedReader is = new BufferedReader(new
                    InputStreamReader(sock.getInputStream()));
            PrintWriter os = new PrintWriter(sock.getOutputStream(), true);
            os.print(message + "\r\n");
            os.flush();
            String reply = is.readLine();
            System.out.println("Sent \"" + message + "\"");
            System.out.println("Got  \"" + reply + "\"");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
