package com.ouyang.fbyx.minio;

import com.alibaba.fastjson.JSON;
import io.minio.*;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ouyangxingjie
 * @Description minio文件上传demo
 * @Date 14:03 2022/2/25
 */
public class FileUploader {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 创建minio客户端
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://139.198.183.194:9000/")
                            .credentials("admin", "admin123456")
                            .build();

            // 首先搜索 test_bucket是否存在
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("testbucket").build());
            if (!found) {
                // 如果不存在则创建名为 test_bucket 的bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("testbucket").build());
            }

            // 将文件命名为 test_obj 上传到 test_bucket
            ObjectWriteResponse response = minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("testbucket")
                            .object("test_obj")
                            .filename("C:\\Users\\Admin\\Desktop\\d46fb9236de9557b29f3f5318658c19.jpg")
                            .build());
            System.out.println("上传成功，返回结果：" + response.etag());
        } catch (MinioException e) {
            System.out.println("上传失败: " + e);
        }
    }
}