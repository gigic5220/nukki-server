package com.done.nukki.service;

import com.done.nukki.exception.S3UploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * AWS S3에 파일을 업로드하는 서비스.
 */
@Service
public class S3Service {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * S3Service 생성자.
     *
     * @param s3Client S3 업로드 및 다운로드를 처리하는 클라이언트
     */
    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * 주어진 파일을 AWS S3에 업로드하고, 업로드된 파일의 URL을 반환한다.
     *
     * @param file 업로드할 파일 (MultipartFile)
     * @return 업로드된 파일의 S3 URL
     * @throws S3UploadException 파일 업로드 실패 시 발생
     * @throws RuntimeException 파일 변환 중 IO 오류가 발생할 경우
     */
    public String uploadFile(MultipartFile file) {
        // 파일명 설정 (원본 파일명 + UUID + timestamp)
        String fileName = file.getOriginalFilename() + "-" + UUID.randomUUID() + "-" + System.currentTimeMillis();

        try {
            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // 파일 업로드 수행
            PutObjectResponse response = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            // 응답 상태 코드 및 요청 정보 로깅
            int statusCode = response.sdkHttpResponse().statusCode();
            String requestId = response.responseMetadata().requestId();
            String etag = response.eTag();

            logger.info("✅ S3 업로드 성공");
            logger.info("📌 응답 코드: {}", statusCode);
            logger.info("📌 Request ID: {}", requestId);
            logger.info("📌 ETag: {}", etag);

            // 업로드 성공 여부 확인 후 URL 반환
            if (response.sdkHttpResponse().isSuccessful()) {
                return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            } else {
                throw new S3UploadException("S3 파일 업로드 실패 - 응답 코드: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 중 오류 발생", e);
        }
    }
}
