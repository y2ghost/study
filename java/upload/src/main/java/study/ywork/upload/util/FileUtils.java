package study.ywork.upload.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import study.ywork.upload.web.vm.FileUploadVM;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private static final String DOT = ".";

    private FileUtils() {
    }

    public static void downloadFile(String name, String path, HttpServletRequest request, HttpServletResponse response)
            throws FileNotFoundException {
        File downloadFile = new File(path);
        String fileName = name;

        if (StringUtils.isBlank(fileName)) {
            fileName = downloadFile.getName();
        }

        String headerValue = String.format("attachment;filename*=utf-8''%s", fileName);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        response.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        // 获取文件大小
        long downloadSize = downloadFile.length();
        long fromPos = 0;
        long toPos = 0;

        if (null != request.getHeader(HttpHeaders.RANGE)) {
            response.setStatus(HttpStatus.PARTIAL_CONTENT.value());
            String range = request.getHeader(HttpHeaders.RANGE);
            String bytes = range.replace("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);

            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }

            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }

            downloadSize = size;
        }

        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(downloadSize));
        try (RandomAccessFile in = new RandomAccessFile(downloadFile, "rw");
             OutputStream out = response.getOutputStream()) {
            in.seek(fromPos);
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num = 0;
            // 当前写入客户端大小
            int count = 0;

            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;

                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize - count);
                    if (bufLen == 0) {
                        break;
                    }
                }
            }

            response.flushBuffer();
        } catch (IOException e) {
            LOG.error("download error: {}", e.getMessage());
        }
    }

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.error("close error: {}", e.getMessage());
            }
        }
    }

    public static void closeMappedByteBuffer(MappedByteBuffer byteBuffer) {
        if (null != byteBuffer) {
            byteBuffer.force();
        }
    }

    public static String buildPath(String rootDir, FileUploadVM param) {
        Path filePath = Paths.get(rootDir, param.getMd5());
        return filePath.toString();
    }

    public static byte[] readFileToByteArray(final File file) throws IOException {
        Objects.requireNonNull(file, "file");
        return Files.readAllBytes(file.toPath());
    }

    public static String getExtension(String fileName) {
        int index = StringUtils.indexOf(fileName, DOT);
        if (StringUtils.INDEX_NOT_FOUND == index) {
            return StringUtils.EMPTY;
        }

        String ext = StringUtils.substring(fileName, index + 1);
        return StringUtils.trimToEmpty(ext);
    }
}
