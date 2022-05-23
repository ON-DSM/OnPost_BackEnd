package com.onpost.global.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onpost.global.error.exception.FileConversionException;
import com.onpost.global.error.exception.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getName();
        String uploadImageUrl = putS3(multipartFile, fileName);
        return uploadImageUrl;
    }

    private String putS3(MultipartFile uploadFile, String fileName) {

        if(uploadFile.isEmpty()) {
            throw FileConversionException.EXCEPTION;
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(uploadFile.getContentType());

        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw FileUploadFailedException.EXCEPTION;
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void delete(String currentFilePath) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, currentFilePath));
        log.info("기존 파일 삭제 완료 : " + currentFilePath);
    }
}
