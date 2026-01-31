package study.ywork.basis.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDemo {
    public static void main(String[] args) {
        String[] urls = {
                "http://www.adc.org",
                "https://www.amazon.com/exec/obidos/order2/",
                "ftp://ibiblio.org/pub/languages/java/javafaq/",
                "mailto:elharo@ibiblio.org",
                "telnet://dibner.poly.edu/",
                "file:///etc/passwd",
                "gopher://gopher.anc.org.za/",
                "ldap://ldap.itd.umich.edu/o=University%20of%20Michigan,c=US?postalAddress",
                "jar:http://cafeaulait.org/books/javaio/ioexamples/javaio.jar!/com/macfaq/io/StreamCopier.class",
                "nfs://utopia.poly.edu/usr/tmp/",
                "jdbc:mysql://luna.ibiblio.org:3306/NEWS",
                "rmi://ibiblio.org/RenderEngine",
                "doc:/UsersGuide/release.html",
                "netdoc:/UsersGuide/release.html",
                "systemresource://www.adc.org/+/index.html",
                "verbatim:http://www.adc.org/",
        };

        for (String url : urls) {
            testProtocol(url);
        }

        System.out.println("======== Get Data test ========");
        getData();
        System.out.println("======== Show Urls Info ========");
        showUrlsInfo();
    }

    private static void testProtocol(String url) {
        try {
            URL u = new URL(url);
            System.out.println(u.getProtocol() + " is supported");
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static void getData() {
        URL url = null;
        try {
            url = new URL("http://www.oreilly.com");
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        try (InputStream in = new BufferedInputStream(url.openStream());
             Reader r = new InputStreamReader(in)) {
            int c = 0;

            while (true) {
                c = r.read();
                if (-1 == c) {
                    break;
                }

                System.out.print((char) c);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void showUrlsInfo() {
        String[] urls = {"ftp://mp3:mp3@138.247.121.61:21000/c%3a/",
                "http://www.oreilly.com",
                "http://www.ibiblio.org/nywc/compositions.phtml?category=Piano",
                "http://admin@www.blackstar.com:8080",};

        URL u;
        for (String url : urls) {
            try {
                u = new URL(url);
            } catch (MalformedURLException ex) {
                System.err.println(ex.getMessage());
                continue;
            }

            showUrlInfo(u);
            System.out.println();
        }
    }

    private static void showUrlInfo(URL u) {
        System.out.println("The URL is " + u);
        System.out.println("The scheme is " + u.getProtocol());
        System.out.println("The user info is " + u.getUserInfo());

        String host = u.getHost();
        if (host != null) {
            int atSign = host.indexOf('@');
            if (atSign != -1) {
                host = host.substring(atSign + 1);
            }

            System.out.println("The host is " + host);
        } else {
            System.out.println("The host is null.");
        }

        System.out.println("The port is " + u.getPort());
        System.out.println("The path is " + u.getPath());
        System.out.println("The ref is " + u.getRef());
        System.out.println("The query string is " + u.getQuery());
    }
}