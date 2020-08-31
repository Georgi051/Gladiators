package project.gladiators.service.impl;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.CloudinaryService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    @Override
    public String uploadImage(MultipartFile multipartfile) throws IOException {
        File file = File.createTempFile("temp-file",multipartfile.getOriginalFilename());
        multipartfile.transferTo(file);
        return this.cloudinary.uploader().upload(file, new HashMap()).get("url").toString();
    }
}
