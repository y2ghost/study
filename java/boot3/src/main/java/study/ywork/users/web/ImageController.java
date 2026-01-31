package study.ywork.users.web;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import study.ywork.users.domain.Image;
import study.ywork.users.repository.ImageDbRepository;

import java.io.IOException;

/**
 * 上传测试
 * curl -H "Content-Type: multipart/form-data" -F "image=@in.png" http://localhost:8080/images
 * 下载测试
 * curl -v http://localhost:8080/images/1 -o out.png
 */
@RestController
@RequestMapping("/images")
class ImageController {
    // 直接保存到数据库，适合很小的图片文件
    private final ImageDbRepository imageDbRepository;

    public ImageController(ImageDbRepository imageDbRepository) {
        this.imageDbRepository = imageDbRepository;
    }

    @PostMapping("")
    public Long uploadImage(@RequestParam("image") MultipartFile multipartImage) throws IOException {
        Image dbImage = new Image();
        dbImage.setName(multipartImage.getName());
        dbImage.setContent(multipartImage.getBytes());
        return imageDbRepository.save(dbImage).getId();
    }

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource downloadImage(@PathVariable Long imageId) {
        byte[] image = imageDbRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();
        return new ByteArrayResource(image);
    }
}
