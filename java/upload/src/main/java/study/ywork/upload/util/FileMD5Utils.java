package study.ywork.upload.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算文件MD5工具类
 */
public class FileMD5Utils {
    private static final Logger LOG = LoggerFactory.getLogger(FileMD5Utils.class);

    public static String getFileMD5(File file) throws FileNotFoundException {
        String value = null;
        MappedByteBuffer byteBuffer = null;

        try (FileInputStream in = new FileInputStream(file)) {
            byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);

            if (value.length() < 32) {
                value = "0" + value;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            FileUtils.closeMappedByteBuffer(byteBuffer);
        }

        return value;
    }

    public static String getFileMD5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            return new BigInteger(1, digest).toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            LOG.error("get file md5 error!!!", e);
        }

        return null;
    }

    private FileMD5Utils() {
    }
}
