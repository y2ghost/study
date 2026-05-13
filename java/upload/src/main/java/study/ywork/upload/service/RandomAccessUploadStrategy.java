package study.ywork.upload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.ywork.upload.util.FileUtils;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

@Service
public class RandomAccessUploadStrategy extends SliceUploadTemplate {
    private final Integer defaultChunkSize;

    public RandomAccessUploadStrategy(@Value("${upload.file.slice.chunk-size}") Integer chunkSize,
                                      @Value("${upload.file.slice.dir}") String sliceDir, RedisService redisService) {
        super(sliceDir, redisService);
        this.defaultChunkSize = chunkSize;
    }

    @Override
    public boolean upload(FileUploadVM param) {
        String uploadDirPath = FileUtils.buildPath(sliceDir, param);
        File tmpFile = super.createTmpFile(param);
        try (RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw")) {
            // 这个必须与前端设定的值一致
            long chunkSize = Objects.isNull(param.getChunkSize()) ? defaultChunkSize * 1024 * 1024
                    : param.getChunkSize();
            long offset = chunkSize * param.getChunk();
            // 定位到该分片的偏移量
            accessTmpFile.seek(offset);
            // 写入该分片数据
            accessTmpFile.write(param.getFile().getBytes());
            return super.checkAndSetUploadProgress(param, uploadDirPath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }
}
