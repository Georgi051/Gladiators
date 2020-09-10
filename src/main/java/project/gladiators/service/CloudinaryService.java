package project.gladiators.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CloudinaryService {
    String uploadImage(MultipartFile multipartfile) throws IOException;

    String uploadImageToCurrentFolder(MultipartFile multipartFile, String folderName) throws IOException;
}
