package com.project.storyapp.services;

import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.PostRepository;
import com.project.storyapp.requests.PostCreateRequest;
import com.project.storyapp.requests.PostUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> getAllPosts(Optional<Long> userId) {
        if(userId.isPresent()) {
            return postRepository.findByUserId(userId.get());
        }
        return postRepository.findAll();
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUser(newPostRequest.getUserId());

        if(user == null) {
            return null;
        }

        Post postToSave = new Post();
        postToSave.setId(newPostRequest.getId());
        postToSave.setText(newPostRequest.getText());
        postToSave.setTitle(newPostRequest.getTitle());
        postToSave.setUser(user);

        return postRepository.save(postToSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest) {
        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()) {
            Post postToUpdate = post.get();
            postToUpdate.setText(postUpdateRequest.getText());
            postToUpdate.setTitle(postUpdateRequest.getTitle());
            postRepository.save(postToUpdate);

            return postToUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
