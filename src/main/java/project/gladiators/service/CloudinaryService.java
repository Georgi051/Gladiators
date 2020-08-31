package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartfile) throws IOException;
}
