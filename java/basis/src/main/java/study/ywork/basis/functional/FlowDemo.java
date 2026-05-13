package study.ywork.basis.functional;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowDemo {
    private static final int BUFFER_COUNT = 1;

    public static void main(String[] args) throws InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        class MySubscriber implements Flow.Subscriber<String> {
            private Flow.Subscription subscription;
            private String id;

            MySubscriber(String id) {
                this.id = id;
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                subscription.request(getNumBuffers());
            }

            @Override
            public void onNext(String item) {
                System.out.println(id + ": Received: " + item);
                subscription.request(getNumBuffers());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println(id + ": Done");
            }
        }

        Flow.Subscriber<String> subscriber = new MySubscriber("1");
        publisher.subscribe(subscriber);
        Flow.Subscriber<String> subscriber2 = new MySubscriber("2");
        publisher.subscribe(subscriber2);

        for (String message : messages) {
            System.out.println("Sending: " + message);
            publisher.submit(message);
        }

        publisher.close();
        Thread.sleep(1000);
    }

    static int getNumBuffers() {
        return BUFFER_COUNT;
    }

    static String[] messages = {
            "Hello, world of Java!",
            "Hope you're having a good day!",
            "Things are really hopping now",
            "The clock is ticking.",
            "Day is done. Goodbye!",
    };
}

