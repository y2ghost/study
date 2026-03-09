package study.ywork.users.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import study.ywork.users.service.FileStreamService;

import java.io.IOException;
import java.util.List;

/**
 * 上传大图片(大文件)时建议采用流式的方式，保证内存占用可用
 * 配置MultipartResolver示例
 * spring.servlet.multipart.max-file-size=10MB
 * spring.servlet.multipart.max-request-size=10MB
 * // 这个配置表示文件直接写入磁盘，或是通过文件流进行处理
 * spring.servlet.multipart.file-size-threshold=0
 * 上传测试
 * curl -H "Content-Type: multipart/form-data" -F "image=@in.png" http://localhost:8080/stream-images
 * 下载测试
 * curl -v http://localhost:8080/stream-images/in.png -o out.png
 * curl -v http://localhost:8080/stream-images/downloadImages?fileNames=in.png,in2.png -o out.data
 */
@RestController
@RequestMapping("/stream-images")
class StreamImageController {
    private final FileStreamService fileStreamService;

    public StreamImageController(FileStreamService fileStreamService) {
        this.fileStreamService = fileStreamService;
    }

    @PostMapping("")
    public ResponseEntity<String> uploadFileStreaming(@RequestPart("image") MultipartFile multipartImage) throws IOException {
        String filename = fileStreamService.saveFile(multipartImage);
        return ResponseEntity.ok("Upload successful: " + filename);
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public StreamingResponseBody downloadImage(@PathVariable String fileName) {
        return stream -> fileStreamService.writeFile(fileName, stream);
    }

    @GetMapping("/downloadImages")
    public StreamingResponseBody downloadFiles(@RequestParam("fileNames") String fileNames, HttpServletResponse response) {
        String boundary = "filesBoundary";
        response.setContentType("multipart/mixed; boundary=" + boundary);
        List<String> tempFileNames = List.of(fileNames.split(","));
        return stream -> fileStreamService.writeFiles(tempFileNames, boundary, stream);
    }
}
