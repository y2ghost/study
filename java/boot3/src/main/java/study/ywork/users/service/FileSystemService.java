package study.ywork.users.service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import study.ywork.users.domain.Image;
import study.ywork.users.repository.FileSystemRepository;
import study.ywork.users.repository.ImageDbRepository;

import java.io.IOException;

@Service
public class FileSystemService {
    private final FileSystemRepository fileSystemRepository;
    private final ImageDbRepository imageDbRepository;

    public FileSystemService(FileSystemRepository fileSystemRepository,
                             ImageDbRepository imageDbRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.imageDbRepository = imageDbRepository;

    }

    public Long save(byte[] bytes, String imageName) throws IOException {
        String location = fileSystemRepository.save(bytes, imageName);
        return imageDbRepository.save(new Image(imageName, location)).getId();
    }

    public Resource find(Long imageId) {
        Image image = imageDbRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
}
