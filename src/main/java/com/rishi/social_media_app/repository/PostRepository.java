package com.rishi.social_media_app.repository;

import com.rishi.social_media_app.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByTitleContainingOrTextContainingOrTagsContaining(String title, String text, String tags, Pageable pageable);

    @Query("{'$text': { '$search': ?0 } }")
    Page<Post> searchByText(String searchTerm, Pageable pageable);
}
