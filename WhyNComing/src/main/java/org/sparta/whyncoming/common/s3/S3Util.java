package org.sparta.whyncoming.common.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
public class S3Util {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3Util(S3Client _s3Client, S3Presigner _s3Presigner,
                  @Value("${cloud.aws.s3.bucket}") String _bucketName){
        this.s3Client = _s3Client;
        this.s3Presigner = _s3Presigner;
        this.bucketName = _bucketName;
    }

    // 파일 S3에 업로드, 파일 url 반환
    public String uploadFile(MultipartFile file) throws IOException{

        // 파일명 없으면 Exception
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()){
            throw new IllegalArgumentException("유효한 파일명이 아닙니다.");
        }

        // 파일명 처리 (충돌 방지)
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

        try{
            // 요청 객체 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // 업로드
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    file.getInputStream(),
                    file.getSize()
            ));

            // 반환
            return generatePresignedUrl(fileName);
        } catch (S3Exception e){
            throw new IOException("파일 업로드 실패", e);
        }
    }

    private String generatePresignedUrl(String fileName){
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(b -> b.bucket(bucketName).key(fileName))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
