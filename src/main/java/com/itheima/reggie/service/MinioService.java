//package com.itheima.reggie.service;
//
//import io.minio.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//
//@Service
//public class MinioService {
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Value("${minio.bucketName}")
//    private String bucketName;
//
//    public void uploadFile(String objectName, byte[] content) throws Exception {
//        ByteArrayInputStream bais = new ByteArrayInputStream(content);
//
//        // 上传文件到指定bucket
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .stream(bais, content.length, -1)
//                        .contentType("application/octet-stream")
//                        .build()
//        );
//        System.out.println("File uploaded successfully: " + objectName);
//    }
//
//    public String getFile(String objectName) throws Exception {
//        minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).build());
//        return "";
//    }
//}
