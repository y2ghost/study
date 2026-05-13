package study.ywork.vertx.stream;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class AudioPlayerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(AudioPlayerVerticle.class);
    private State currentMode = State.PAUSED;
    private final Queue<String> playlist = new ArrayDeque<>();
    private final Set<HttpServerResponse> streamers = new HashSet<>();
    private AsyncFile currentFile;
    private long positionInFile;

    private enum State {
        PLAYING, PAUSED
    }

    @Override
    public void start() {
        logger.info("开始");
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("audio.list", this::list);
        eventBus.consumer("audio.schedule", this::schedule);
        eventBus.consumer("audio.play", this::play);
        eventBus.consumer("audio.pause", this::pause);
        vertx.createHttpServer().requestHandler(this::httpHandler).listen(8080);
        vertx.setPeriodic(100, this::streamAudioChunk);
    }

    private void list(Message<?> request) {
        vertx.fileSystem().readDir("tracks", ".*mp3$", ar -> {
            if (ar.succeeded()) {
                List<String> files = ar.result().stream().map(File::new).map(File::getName).toList();
                JsonObject json = new JsonObject().put("files", new JsonArray(files));
                request.reply(json);
            } else {
                logger.error("歌曲目录读取失败", ar.cause());
                request.fail(500, ar.cause().getMessage());
            }
        });
    }

    private void play(Message<?> request) {
        logger.info("播放");
        currentMode = State.PLAYING;
    }

    private void pause(Message<?> request) {
        logger.info("暂停");
        currentMode = State.PAUSED;
    }

    private void schedule(Message<JsonObject> request) {
        String file = request.body().getString("file");
        logger.info("加载 {}", file);

        if (playlist.isEmpty() && currentMode == State.PAUSED) {
            currentMode = State.PLAYING;
        }

        playlist.offer(file);
    }

    private void httpHandler(HttpServerRequest request) {
        String path = request.path();
        logger.info("{} '{}' {}", request.method(), path, request.remoteAddress());

        if ("/".equals(path)) {
            openAudioStream(request);
            return;
        }

        if (path.startsWith("/download/")) {
            String sanitizedPath = path.substring(10).replace("/", "");
            download(sanitizedPath, request);
            return;
        }

        request.response().setStatusCode(404).end();
    }

    private void openAudioStream(HttpServerRequest request) {
        logger.info("打开音频流");
        HttpServerResponse response = request.response().putHeader("Content-Type", "audio/mpeg").setChunked(true);
        streamers.add(response);
        response.endHandler(v -> {
            streamers.remove(response);
            logger.info("关闭音频流");
        });
    }

    private void download(String path, HttpServerRequest request) {
        String file = "tracks/" + path;
        if (!vertx.fileSystem().existsBlocking(file)) {
            request.response().setStatusCode(404).end();
            return;
        }

        OpenOptions opts = new OpenOptions().setRead(true);
        vertx.fileSystem().open(file, opts, ar -> {
            if (ar.succeeded()) {
                downloadFile(ar.result(), request);
            } else {
                logger.error("音频文件读取失败", ar.cause());
                request.response().setStatusCode(500).end();
            }
        });
    }

    private void downloadFile(AsyncFile file, HttpServerRequest request) {
        HttpServerResponse response = request.response();
        response.setStatusCode(200).putHeader("Content-Type", "audio/mpeg").setChunked(true);

        file.handler(buffer -> {
            response.write(buffer);
            if (response.writeQueueFull()) {
                file.pause();
                response.drainHandler(v -> file.resume());
            }
        });

        file.endHandler(v -> response.end());
    }

    private void streamAudioChunk(long id) {
        if (currentMode == State.PAUSED) {
            return;
        }

        if (currentFile == null && playlist.isEmpty()) {
            currentMode = State.PAUSED;
            return;
        }

        if (currentFile == null) {
            openNextFile();
        }

        currentFile.read(Buffer.buffer(4096), 0, positionInFile, 4096, ar -> {
            if (ar.succeeded()) {
                processReadBuffer(ar.result());
            } else {
                logger.error("文件读取失败", ar.cause());
                closeCurrentFile();
            }
        });
    }

    private void openNextFile() {
        String file = playlist.peek();
        logger.info("打开文件 {}", file);
        OpenOptions opts = new OpenOptions().setRead(true);
        currentFile = vertx.fileSystem().openBlocking("tracks/" + playlist.poll(), opts);
        positionInFile = 0;
    }

    private void closeCurrentFile() {
        logger.info("关闭文件");
        positionInFile = 0;
        currentFile.close();
        currentFile = null;
    }

    private void processReadBuffer(Buffer buffer) {
        logger.info("读取字节数 {} 文件偏移位置 {}", buffer.length(), positionInFile);
        positionInFile += buffer.length();

        if (buffer.length() == 0) {
            closeCurrentFile();
            return;
        }

        for (HttpServerResponse streamer : streamers) {
            if (!streamer.writeQueueFull()) {
                streamer.write(buffer.copy());
            }
        }
    }
}
