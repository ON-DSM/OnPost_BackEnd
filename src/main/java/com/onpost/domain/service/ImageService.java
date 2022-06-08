package com.onpost.domain.service;

import com.onpost.domain.entity.Image;
import com.onpost.domain.repository.ImageRepository;
import com.onpost.global.aws.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public record ImageService(S3Uploader s3Uploader, ImageRepository imageRepository) {

    public String addImage(MultipartFile image, String dirName) {
        return s3Uploader.upload(image, dirName);
    }

    public String getPath(MultipartFile file, String dirName) {
        Image image = Image.builder().imagePath(s3Uploader.upload(file, dirName)).build();
        imageRepository.save(image);
        return image.getImagePath();
    }

    public void deletePath(String path) {
        String[] paths = path.split("/");
        s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
    }
}
