package study.ywork.basis.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {
    private static final String HOST = "www.baidu.com";

    public static void main(String[] args) {
        getHostName(HOST);
        getLocalHost();
        getAddress();
    }

    public static void getHostName(String host) {
        InetAddress address;
        try {
            address = InetAddress.getByName(host);
            System.out.println(address);
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void getLocalHost() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address);
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void getAddress() {
        try {
            InetAddress ia = InetAddress.getByName(HOST);
            System.out.println("CanonicalHostName: " + ia.getCanonicalHostName());
            InetAddress me = InetAddress.getLocalHost();
            String ip = me.getHostAddress();
            String ipInfo = String.format("My address: %s, Type: %d", ip, getVersion(me));
            System.out.println(ipInfo);
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /* 获取InetAddress对象的地址版本：IPV4，还是IPV6 */
    public static int getVersion(InetAddress ia) {
        int type = -1;
        byte[] address = ia.getAddress();

        switch (address.length) {
        case 4:
            type = 4;
            break;
        case 16:
            type = 6;
            break;
        default:
            type = -1;
            break;
        }

        return type;
    }
}