package com.project.storyapp.services;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.LikeRepository;
import com.project.storyapp.requests.LikeCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LikeService {

    private LikeRepository likeRepository;
    private UserService userService;
    private PostService postService;

    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<Like> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        if(userId.isPresent() && postId.isPresent()) {
            return likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        }else if(userId.isPresent()) {
            return likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            return likeRepository.findByPostId(postId.get());
        }

        return likeRepository.findAll();
    }

    public Like getOneLikeById(Long LikeId) {
        return likeRepository.findById(LikeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest likeCreateRequest) {
        User user = userService.getOneUserById(likeCreateRequest.getUserId());
        Post post = postService.getOnePostById(likeCreateRequest.getPostId());
        if(Objects.nonNull(user) && Objects.nonNull(post)) {
            Like likeToSave = new Like();
            likeToSave.setId(likeCreateRequest.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);

            return likeRepository.save(likeToSave);
        }
        return null;
    }

    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}
