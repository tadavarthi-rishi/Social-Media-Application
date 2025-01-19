package com.rishi.social_media_app.service;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.rishi.social_media_app.config.AWSConfig;
import com.rishi.social_media_app.model.User;
import com.rishi.social_media_app.model.UserDto;
import com.rishi.social_media_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService  {

    private final UserRepository userRepository;
    private final S3Client s3Client;

    public User register(UserDto userDto) throws IOException {

        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        var uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        var mediaFile = userDto.profilePhoto();
        if (mediaFile != null && !mediaFile.isEmpty()) {
            String fileName = uuid+"-profile";
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(AWSConfig.BUCKET_NAME)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(mediaFile.getBytes()));
            user.setProfilePhoto(fileName);
        }


        return userRepository.save(user);


    }
}
