package project.gladiators.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.gladiators.service.CloudinaryService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String uploadImageToCurrentFolder(MultipartFile multipartFile ,String folderName) throws IOException {
        String file = multipartFile.getOriginalFilename();
      
        File toUpload = File.createTempFile("temp-file",multipartFile.getOriginalFilename());
        multipartFile.transferTo(toUpload);
        Map params = ObjectUtils.asMap("public_id", folderName+"/"+file);

     return cloudinary.uploader().upload(toUpload, params).get("url").toString();


    }
}
