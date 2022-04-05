package com.onpost.domain.service;

import com.onpost.domain.entity.Image;
import com.onpost.domain.repository.jpa.ImageRepository;
import com.onpost.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    public void deleteImageList(List<Long> imageList) {
        for(Long im : imageList) {
            Image image = imageRepository.getById(im);
            String[] paths = image.getImagePath().split("/");
            s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
            imageRepository.delete(image);
        }
    }

    public void deleteImageList(Set<Image> imageSet) {
        for(Image image : imageSet) {
            String[] paths = image.getImagePath().split("/");
            s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
            imageRepository.delete(image);
        }
    }

    public void deleteImage(Image image) {
        String[] paths = image.getImagePath().split("/");
        s3Uploader.delete(paths[paths.length - 2] + "/" + paths[paths.length - 1]);
        imageRepository.delete(image);
    }

    public Set<Image> getImageList(List<MultipartFile> fileList, String dirName) {
        LinkedHashSet<Image> imageList = new LinkedHashSet<>();
        for(MultipartFile im : fileList) {
            imageList.add(imageRepository.save(Image.builder().imagePath(s3Uploader.upload(im, dirName)).build()));
        }
        return imageList;
    }

    public Image getImage(MultipartFile file, String dirName) {
        return imageRepository.save(
                Image.builder().imagePath(s3Uploader.upload(file, dirName)).build()
        );
    }
}
