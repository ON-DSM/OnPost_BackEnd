package com.onpost.domain.service;

import com.onpost.domain.entity.Image;
import com.onpost.domain.repository.jpa.ImageRepository;
import com.onpost.global.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public record ImageService(S3Uploader s3Uploader, ImageRepository imageRepository) {

    public void deleteImageList(Set<Image> images) {
        for (Image image : images) {
            String[] paths = image.getImagePath().split("/");
            s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
            imageRepository.delete(image);
        }
    }

    public Set<Image> getImageList(List<MultipartFile> images, String dirName) {
        Set<Image> imageList = new LinkedHashSet<>();
        if (images != null) {
            for (MultipartFile im : images) {
                imageList.add(imageRepository.save(Image.builder().imagePath(s3Uploader.upload(im, dirName)).build()));
            }
        }
        return imageList;
    }

    public String getPath(MultipartFile file, String dirName) {
        return s3Uploader.upload(file, dirName);
    }

    public void deletePath(String path) {
        String[] paths = path.split("/");
        s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
    }
}
