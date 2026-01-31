package study.ywork.vertx.stream;

import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;

public class VertxStream {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        OpenOptions opts = new OpenOptions().setRead(true);
        vertx.fileSystem().open("build.gradle", opts, ar -> {
            if (ar.succeeded()) {
                AsyncFile file = ar.result();
                file.handler(System.out::println).exceptionHandler(Throwable::printStackTrace).endHandler(done -> {
                    System.out.println("\n--- 完成");
                    vertx.close();
                });
            } else {
                ar.cause().printStackTrace();
            }
        });
    }
}
