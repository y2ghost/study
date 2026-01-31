package study.ywork.users.repository;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import study.ywork.users.exception.ServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 图片存储到文件系统
 */
@Repository
public class FileSystemRepository {
    // 此处简单示例，根据实际业务设置存储图片目录
    private static final String RESOURCES_DIR = "images";

    public String save(byte[] content, String imageName) throws IOException {
        Path newFile = Paths.get(RESOURCES_DIR, new Date().getTime() + "-" + imageName);
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }

    public Resource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new ServiceException();
        }
    }
}
