package study.ywork.basis.api.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

// 递归监控一个目录的变化
public class WatchDirectoryRecursive {
    private static Map<WatchKey, Path> keyPathMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            registerDir(Paths.get("./samples"), watchService);
            startListening(watchService);
        }
    }

    private static void registerDir(Path path, WatchService watchService) throws IOException {
        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }

        System.out.println("registering: " + path);
        WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        keyPathMap.put(key, path);

        for (File f : path.toFile().listFiles()) {
            registerDir(f.toPath(), watchService);
        }
    }

    private static void startListening(WatchService watchService) throws InterruptedException, IOException {
        while (true) {
            WatchKey queuedKey = watchService.take();
            for (WatchEvent<?> watchEvent : queuedKey.pollEvents()) {
                System.out.printf("Event... kind=%s, count=%d, context=%s Context type=%s%n", watchEvent.kind(),
                        watchEvent.count(), watchEvent.context(), ((Path) watchEvent.context()).getClass());
                // 此处可以进行业务处理
                if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    // 此处非完整路径
                    Path path = (Path) watchEvent.context();
                    // 需要获取父级路径
                    Path parentPath = keyPathMap.get(queuedKey);
                    // 然后构造完整路径信息
                    path = parentPath.resolve(path);
                    registerDir(path, watchService);
                }
            }

            if (!queuedKey.reset()) {
                keyPathMap.remove(queuedKey);
            }

            if (keyPathMap.isEmpty()) {
                break;
            }
        }
    }
}
