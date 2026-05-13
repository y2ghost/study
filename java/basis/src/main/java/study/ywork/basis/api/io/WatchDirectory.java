package study.ywork.basis.api.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

// 监控一个目录的变化
public class WatchDirectory {
    public static void main(String[] args) {
        Path path = Paths.get("./samples");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            startListening(watchService);
            System.out.println(key);
        } catch (InterruptedException | IOException e) {
            System.err.println("start listen error " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("done");
    }

    private static void startListening(WatchService watchService) throws InterruptedException {
        boolean isReseted = false;
        while (!isReseted) {
            WatchKey queuedKey = watchService.take();
            for (WatchEvent<?> watchEvent : queuedKey.pollEvents()) {
                System.out.printf("kind=%s, count=%d, context=%s Context type=%s%n ", watchEvent.kind(),
                        watchEvent.count(), watchEvent.context(), ((Path) watchEvent.context()).getClass());
                // 此处可以做业务处理
                isReseted = queuedKey.reset();
            }
        }

        System.out.println("watch quit");
    }
}
