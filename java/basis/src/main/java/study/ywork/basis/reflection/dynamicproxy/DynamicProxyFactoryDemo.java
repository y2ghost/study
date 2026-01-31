package study.ywork.basis.reflection.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxyFactoryDemo {
    static class QuoteServerFactory {
        public static QuoteServer getInstance() {
            QuoteServer objectBeingProxied = new QuoteServerImpl();
            InvocationHandler handler = new MyInvocationHandler(objectBeingProxied);
            return (QuoteServer) Proxy.newProxyInstance(
                    QuoteServer.class.getClassLoader(),
                    new Class[]{QuoteServer.class}, handler);
        }

        private QuoteServerFactory() {
            // 不做事儿
        }
    }

    public static void main(String[] args) {
        QuoteServer quoter = QuoteServerFactory.getInstance();
        System.out.println("QuoteServer object is " + quoter.getClass().getName());
        quoter.addQuote("Only the educated are free -- Epictetus");
        System.out.println("QuoteServer returned: " + quoter.getQuote());
    }
}
