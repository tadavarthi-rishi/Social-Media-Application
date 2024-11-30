package com.rishi.social_media_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("post")
public class Post {
    @Id
    private String id;
    private String title;
    private String text;
    private List<String> tags;
    private int likes;
    private PostCreator creator;

}

