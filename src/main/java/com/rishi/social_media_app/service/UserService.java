package com.rishi.social_media_app.service;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.rishi.social_media_app.config.AWSConfig;
import com.rishi.social_media_app.model.LoginDto;
import com.rishi.social_media_app.model.LoginResponse;
import com.rishi.social_media_app.model.User;
import com.rishi.social_media_app.model.UserDto;
import com.rishi.social_media_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService  {

    private final UserRepository userRepository;
    private final S3Client s3Client;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public User register(UserDto userDto) throws IOException {

        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        var uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        user.setRoles(List.of("ROLE_USER"));
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
    public LoginResponse authenticate(LoginDto input){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password()));
    var user = (User) authentication.getPrincipal();
    String token = tokenService.generateToken(authentication);
    return new LoginResponse(token, user.getName(), user.getEmail(), user.getProfilePhoto());
    }

}
