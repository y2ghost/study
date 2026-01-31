package study.ywork.upload.service;

import org.springframework.stereotype.Service;
import study.ywork.upload.domain.FileChunk;
import study.ywork.upload.domain.FileModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Sha256UploadService {
    // 模拟数据库操作 FileChunk
    private final Map<Integer, FileChunk> chunkMap;
    private Integer chunkGlobalId = 1;

    // 模拟数据库操作 FileModel
    private final Map<Integer, FileModel> fileMap;
    private Integer fileGlobalId = 1;

    public Sha256UploadService() {
        chunkMap = new ConcurrentHashMap<>();
        fileMap = new ConcurrentHashMap<>();
    }

    public Integer addChunk(FileChunk fileChunk) {
        fileChunk.setId(chunkGlobalId++);
        chunkMap.put(fileChunk.getId(), fileChunk);

        return chunkGlobalId;
    }

    public Integer getChunkNumByContentHash(String contentHash) {
        long count = chunkMap.values().stream().filter((chunk) -> chunk.getFileHash().equals(contentHash)).count();
        return Math.toIntExact(count);
    }

    public Boolean removeChunkRecord(String chunkHash) {
        return chunkMap.values().removeIf(chunk -> chunk.getFileHash().equals(chunkHash));
    }

    public Boolean verifyContentHash(Long fileSize, String contentHash) {
        return fileMap.values().stream()
                .anyMatch(file -> file.getContentHash().equals(contentHash) && file.getFileSize().equals(fileSize));
    }

    public List<FileChunk> getChunkListByContentHash(String contentHash) {
        return chunkMap.values().stream().filter(chunk -> chunk.getFileHash().equals(contentHash)).toList();
    }

    public Integer addFile(FileModel fileModel) {
        fileModel.setId(fileGlobalId++);
        fileMap.put(fileModel.getId(), fileModel);
        return fileGlobalId;
    }
}
