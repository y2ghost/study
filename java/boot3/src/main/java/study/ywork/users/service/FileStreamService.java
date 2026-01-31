package study.ywork.users.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileStreamService {
    private static final Path UPLOAD_DIR = Path.of("uploads");

    public String saveFile(MultipartFile filePart) throws IOException {
        String filename = filePart.getOriginalFilename();
        Path targetPath = UPLOAD_DIR.resolve(filename);
        Files.createDirectories(targetPath.getParent());

        try (InputStream inputStream = filePart.getInputStream();
             OutputStream outputStream = Files.newOutputStream(targetPath)) {
            inputStream.transferTo(outputStream);
        }

        return filename;
    }

    public void writeFile(String fileName, OutputStream outputStream) throws IOException {
        try (InputStream inputStream = Files.newInputStream(UPLOAD_DIR.resolve(fileName))) {
            inputStream.transferTo(outputStream);
        }
    }

    public void writeFiles(List<String> fileNames, String boundary, OutputStream outputStream) throws IOException {
        List<Path> files = fileNames.stream().map(UPLOAD_DIR::resolve).toList();
        try (BufferedOutputStream bos = new BufferedOutputStream(outputStream);
             OutputStreamWriter writer = new OutputStreamWriter(bos)) {
            for (Path file : files) {
                writer.write("--" + boundary + "\r\n");
                writer.write("Content-Type: application/octet-stream\r\n");
                writer.write("Content-Disposition: attachment; filename=\"" + file.getFileName() + "\"\r\n\r\n");
                writer.flush();
                Files.copy(file, bos);
                bos.write("\r\n".getBytes());
                bos.flush();
            }

            writer.write("--" + boundary + "--\r\n");
            writer.flush();
        }
    }
}
