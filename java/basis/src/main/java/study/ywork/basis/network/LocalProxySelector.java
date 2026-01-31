package study.ywork.basis.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LocalProxySelector extends ProxySelector {
    private final List<URI> failed = new ArrayList<>();
    private final SocketAddress proxyAddress = new InetSocketAddress("proxy.example.com", 8000);

    @Override
    public List<Proxy> select(URI uri) {
        List<Proxy> result = new ArrayList<>();
        if (!"http".equalsIgnoreCase(uri.getScheme()) || failed.contains(uri)) {
            result.add(Proxy.NO_PROXY);
        } else {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
            result.add(proxy);
        }

        return result;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress address, IOException ex) {
        failed.add(uri);
    }
}