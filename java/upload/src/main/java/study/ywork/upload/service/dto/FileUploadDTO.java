package study.ywork.upload.service.dto;

import java.util.List;
import java.util.Map;

public class FileUploadDTO {

    private String path;

    private Long mtime;

    private boolean uploadComplete;

    private int code;

    private Map<Integer, String> chunkMd5Info;

    private List<Integer> missChunks;

    private long size;

    private String fileExt;

    private String fileId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    public boolean isUploadComplete() {
        return uploadComplete;
    }

    public void setUploadComplete(boolean uploadComplete) {
        this.uploadComplete = uploadComplete;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<Integer, String> getChunkMd5Info() {
        return chunkMd5Info;
    }

    public void setChunkMd5Info(Map<Integer, String> chunkMd5Info) {
        this.chunkMd5Info = chunkMd5Info;
    }

    public List<Integer> getMissChunks() {
        return missChunks;
    }

    public void setMissChunks(List<Integer> missChunks) {
        this.missChunks = missChunks;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
