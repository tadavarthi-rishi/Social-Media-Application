package com.rishi.social_media_app.service;

import com.rishi.social_media_app.model.Post;
import com.rishi.social_media_app.model.PostCreator;
import com.rishi.social_media_app.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
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
