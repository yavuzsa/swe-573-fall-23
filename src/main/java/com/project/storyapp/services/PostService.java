package com.project.storyapp.services;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.PostRepository;
import com.project.storyapp.requests.PostCreateRequest;
import com.project.storyapp.requests.PostUpdateRequest;
import com.project.storyapp.responses.LikeResponse;
import com.project.storyapp.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeService likeService;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService, @Lazy LikeService likeService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> postList;
        if(userId.isPresent()) {
            postList = postRepository.findByUserId(userId.get());
        } else {
            postList = postRepository.findAll();
        }
        return postList.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikes(Optional.empty(),Optional.of(p.getId()));
            return new PostResponse(p,likes);}).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUserById(newPostRequest.getUserId());

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
