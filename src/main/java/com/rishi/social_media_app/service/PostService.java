package com.rishi.social_media_app.service;

import com.rishi.social_media_app.config.AWSConfig;
import com.rishi.social_media_app.model.MediaType;
import com.rishi.social_media_app.model.Post;
import com.rishi.social_media_app.model.PostCreator;
import com.rishi.social_media_app.repository.PostRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    private final S3Client s3Client;

    public Post createPost(String title, String text, List<String> tags, MultipartFile mediaFile) throws IOException {

        String fileName = UUID.randomUUID().toString() + "-" + mediaFile.getOriginalFilename();
        if(mediaFile!=null && !mediaFile.isEmpty()){
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(AWSConfig.BUCKET_NAME)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(mediaFile.getBytes()));
        }
        PostCreator creator = PostCreator.builder()
                .id("1")
                .name("Rishi")
                .build();

        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        post.setLikes(0);
        post.setCreator(creator);
        post.setCreatedAt(java.time.LocalDateTime.now());
        post.setMediaUrl(fileName);
        MediaType mediaType = mediaFile.getContentType().startsWith("video/")? MediaType.VIDEO:(mediaFile.getContentType().startsWith("image/")? MediaType.IMAGE: null);
        post.setMediaType(mediaType);
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size, String searchCriteria){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        if(StringUtils.isEmpty(searchCriteria)){
            return postRepository.findAll(PageRequest.of(page, size, sort));
        }
        else{
            Query query = new Query();
            query.addCriteria(Criteria.where("title").regex(searchCriteria));
            mongoTemplate.find(query, Post.class);

            return postRepository.searchByText(searchCriteria,PageRequest.of(page, size, sort));
        }
    }

    public Post getPostById(String id){
        return postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post not found"));
    }
    public Post updatePost(String id, String title, String text, List<String> tags){
        Post post = getPostById(id);
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        return postRepository.save(post);
    }

    public void deletePost(String id){
        postRepository.deleteById(id);
    }

}
