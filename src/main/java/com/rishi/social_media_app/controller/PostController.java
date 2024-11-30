package com.rishi.social_media_app.controller;

import com.rishi.social_media_app.model.Post;
import com.rishi.social_media_app.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestParam String title, @RequestParam String text, @RequestParam List<String> tags) {
        return postService.createPost(title, text, tags);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable String id){
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable String id, @RequestParam String title, @RequestParam String text, @RequestParam List<String> tags) {
        return postService.updatePost(id, title, text, tags);
    }

    @PostMapping("/{id}/delete")
    public void deletePost(@PathVariable String id){
        postService.deletePost(id);
    }


}
