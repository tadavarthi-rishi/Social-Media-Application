package com.rishi.social_media_app.service;

import com.rishi.social_media_app.config.AWSConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@AllArgsConstructor
public class S3PresignedUrlService {

    public String generatePresignedUrl(String key){
        S3Presigner presigner = S3Presigner.builder().region(Region.US_EAST_1).build();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(AWSConfig.BUCKET_NAME)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(10))
                .getObjectRequest(getObjectRequest)
                .build();
        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toExternalForm();
    }
}
