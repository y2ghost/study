package study.ywork.basis.reflection.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyDemo {
    public static void main(String[] args) {

        QuoteServer quoteServer = getQuoteServer();

        System.out.println("QuoteServer object is " + quoteServer.getClass().getName());
        quoteServer.addQuote("Only the educated are free -- Epictetus");
        System.out.println("QuoteServer returned: " + quoteServer.getQuote());
    }

    public static QuoteServer getQuoteServer() {
        QuoteServer objectBeingProxied = new QuoteServerImpl();
        InvocationHandler handler = new MyInvocationHandler(objectBeingProxied);
        return (QuoteServer) Proxy.newProxyInstance(
                QuoteServer.class.getClassLoader(),
                new Class[]{QuoteServer.class}, handler);
    }
}
