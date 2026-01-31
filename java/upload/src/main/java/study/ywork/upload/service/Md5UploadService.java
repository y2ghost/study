package study.ywork.upload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import study.ywork.upload.constant.FileConstants;
import study.ywork.upload.enumeration.FileCheckMd5Status;
import study.ywork.upload.enumeration.UploadMode;
import study.ywork.upload.exception.BusinessException;
import study.ywork.upload.service.dto.FileUploadDTO;
import study.ywork.upload.util.FileMD5Utils;
import study.ywork.upload.util.FileUtils;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Md5UploadService {
    private final Logger log = LoggerFactory.getLogger(Md5UploadService.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final String sliceDir;
    private final RedisService redisService;
    private final ExecutorService executorService;
    private final CompletionService<FileUploadDTO> completionService;
    private final Map<UploadMode, SliceUploadStrategy> uploadStrategyMap;

    public Md5UploadService(@Value("${upload.file.thread.max-size}") Integer maxThreadSize,
                            @Value("${upload.file.queue.max-size}") Integer maxQueueSize,
                            @Value("${upload.file.slice.dir}") String sliceDir, RedisService redisService,
                            RandomAccessUploadStrategy randomUploadStrategy, MappedByteBufferUploadStrategy bufferUploadStrategy) {
        this.sliceDir = sliceDir;
        this.redisService = redisService;
        uploadStrategyMap = new EnumMap<>(UploadMode.class);
        uploadStrategyMap.put(UploadMode.RANDOM_ACCESS, randomUploadStrategy);
        uploadStrategyMap.put(UploadMode.MAPPED_BYTEBUFFER, bufferUploadStrategy);
        executorService = Executors.newFixedThreadPool(maxThreadSize, r -> {
            String threadName = "uploadPool-" + atomicInteger.getAndIncrement();
            Thread thread = new Thread(r);
            thread.setName(threadName);
            return thread;
        });
        completionService = new ExecutorCompletionService<>(executorService, new LinkedBlockingDeque<>(maxQueueSize));
    }

    public FileUploadDTO upload(FileUploadVM param) throws IOException {
        if (Objects.isNull(param.getFile())) {
            throw new BusinessException("file can not be empty");
        }

        String md5 = FileMD5Utils.getFileMD5(param.getFile());
        param.setMd5(md5);

        String filePath = FileUtils.buildPath(sliceDir, param);
        File targetFile = new File(filePath);

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        String path = Paths.get(filePath, param.getFile().getOriginalFilename()).toString();
        try (FileOutputStream out = new FileOutputStream(path)) {
            out.write(param.getFile().getBytes());
            out.flush();
        }

        redisService.hashSet(FileConstants.FILE_UPLOAD_STATUS, md5, "true");
        FileUploadDTO uploadDTO = new FileUploadDTO();
        uploadDTO.setPath(path);
        uploadDTO.setMtime(Instant.now().getEpochSecond());
        uploadDTO.setUploadComplete(true);
        return uploadDTO;
    }

    public FileUploadDTO sliceUpload(FileUploadVM uploadVM) {
        SliceUploadStrategy uploadStrategy = this.uploadStrategyMap.get(UploadMode.RANDOM_ACCESS);
        try {
            completionService.submit(new FileCallable(uploadStrategy, uploadVM));
            return completionService.take().get();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public FileUploadDTO checkFileMd5(FileUploadVM param) {
        Object uploadProgressObj = redisService.hashGet(FileConstants.FILE_UPLOAD_STATUS, param.getMd5());
        if (uploadProgressObj == null) {
            FileUploadDTO uploadDTO = new FileUploadDTO();
            uploadDTO.setCode(FileCheckMd5Status.FILE_NO_UPLOAD.getValue());
            return uploadDTO;
        }
        String processingStr = uploadProgressObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = String.valueOf(redisService.get(FileConstants.FILE_MD5_KEY + param.getMd5()));
        return fillFileUploadDTO(param, processing, value);
    }

    private FileUploadDTO fillFileUploadDTO(FileUploadVM param, boolean processing, String value) {
        if (processing) {
            String path = FileUtils.buildPath(sliceDir, param);
            FileUploadDTO uploadDTO = new FileUploadDTO();
            uploadDTO.setCode(FileCheckMd5Status.FILE_UPLOADED.getValue());
            uploadDTO.setUploadComplete(true);
            uploadDTO.setPath(path);
            return uploadDTO;
        }

        try {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);

            List<Integer> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i);
                }
            }

            FileUploadDTO uploadDTO = new FileUploadDTO();
            uploadDTO.setCode(FileCheckMd5Status.FILE_UPLOAD_SOME.getValue());
            uploadDTO.setMissChunks(missChunkList);
            return uploadDTO;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
