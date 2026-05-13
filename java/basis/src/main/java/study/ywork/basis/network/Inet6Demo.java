package study.ywork.basis.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.Socket;

public class Inet6Demo {
    public static final int DAYTIME_PORT = 13;
    private static final String HOST = "darian6";

    public static void main(String[] args) throws IOException {
        Inet6Address server = Inet6Address.getByAddress(HOST, null, null);
        System.out.println("Lookup: " + server);

        Socket clientSocket;
        clientSocket = new Socket("fe80::203:93ff:fe07:3ef6", DAYTIME_PORT);
        System.out.println(clientSocket);

        BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String dateString = is.readLine();
        System.out.println(dateString);
        is.close();
        clientSocket.close();
    }
}
