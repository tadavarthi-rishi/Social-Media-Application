package com.rishi.social_media_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("post")
public class Post {

    @Id
    private String id;
    private String title;
    private String text;
    private List<String> tags;
    private LocalDateTime createdAt;
    private int likes;
    private PostCreator creator;
    private String mediaUrl;

    @Transient
    private String presignedUrl;
    private MediaType mediaType;

}

