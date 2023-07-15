package study.ywork.vertx.stream;

import io.vertx.core.Vertx;

public class Player {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AudioPlayerVerticle());
        vertx.deployVerticle(new NetVerticle());
    }
}
