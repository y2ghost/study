package study.ywork.multi.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {
    public static void main(String... args) throws InterruptedException {
        Exchanger<MyExchangeData> exchanger = new Exchanger<MyExchangeData>();
        ExecutorService es = Executors.newFixedThreadPool(2);

        es.execute(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("-- party1 next --");
                    MyExchangeData data = new MyExchangeData("msg from party1 " + i);
                    System.out.println("party1 calling exchange() with data: " + data);
                    MyExchangeData exchange = exchanger.exchange(data);
                    System.out.println("party1 exchange() returned and received: " + exchange);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });
        Thread.sleep(1000);

        es.execute(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("-- party2 next --");
                    MyExchangeData data = new MyExchangeData("msg from party2 " + i);
                    System.out.println("party2 calling exchange() with data: " + data);
                    MyExchangeData exchange = exchanger.exchange(data);
                    System.out.println("party2 exchange() returned and received: " + exchange);
                    Thread.sleep(1000);
                }

                es.shutdown();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        });
    }

    private static class MyExchangeData {
        private String msg;

        public MyExchangeData(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "MyExchangeData{" + "msg='" + msg + '\'' + '}';
        }
    }
}
