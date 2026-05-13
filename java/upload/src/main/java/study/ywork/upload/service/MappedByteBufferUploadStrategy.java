package study.ywork.upload.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.ywork.upload.util.FileUtils;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

@Service
public class MappedByteBufferUploadStrategy extends SliceUploadTemplate {
    private final Integer defaultChunkSize;

    public MappedByteBufferUploadStrategy(@Value("${upload.file.slice.chunk-size}") Integer chunkSize,
                                          @Value("${upload.file.slice.dir}") String sliceDir, RedisService redisService) {
        super(sliceDir, redisService);
        this.defaultChunkSize = chunkSize;
    }

    @Override
    public boolean upload(FileUploadVM param) {
        String uploadDirPath = FileUtils.buildPath(sliceDir, param);
        File tmpFile = super.createTmpFile(param);

        try (RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw")) {
            FileChannel fileChannel = tempRaf.getChannel();
            long chunkSize = Objects.isNull(param.getChunkSize()) ? defaultChunkSize * 1024 * 1024
                    : param.getChunkSize();
            // 写入该分片数据
            long offset = chunkSize * param.getChunk();
            byte[] fileData = param.getFile().getBytes();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset,
                    fileData.length);
            mappedByteBuffer.put(fileData);
            boolean isOk = super.checkAndSetUploadProgress(param, uploadDirPath);
            FileUtils.closeMappedByteBuffer(mappedByteBuffer);
            return isOk;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }
}