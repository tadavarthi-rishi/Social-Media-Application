package com.rishi.social_media_app.service;

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

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;

    public Post createPost(String title, String text, List<String> tags){

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
