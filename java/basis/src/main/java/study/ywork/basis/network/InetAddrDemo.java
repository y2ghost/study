package study.ywork.basis.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class InetAddrDemo {
    public static void main(String[] args) throws IOException {
        String hostName = "baidu.com";
        String ipNumber = "8.8.8.8";
        System.out.println(hostName + "'s address is " +
                InetAddress.getByName(hostName).getHostAddress());

        System.out.println(ipNumber + "'s name is " +
                InetAddress.getByName(ipNumber).getHostName());

        final InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("My localhost address is " + localHost);

        String someServerName = "google.com";
        try (Socket theSocket = new Socket(someServerName, 80)) {
            InetAddress remote = theSocket.getInetAddress();
            System.out.printf("The InetAddress for %s is %s%n",
                    someServerName, remote);
        }
    }
}

