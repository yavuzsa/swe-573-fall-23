package com.project.storyapp.services;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.LikeRepository;
import com.project.storyapp.requests.LikeCreateRequest;
import com.project.storyapp.responses.LikeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<LikeResponse> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        List<Like> likeList;
        if(userId.isPresent() && postId.isPresent()) {
            likeList = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        }else if(userId.isPresent()) {
            likeList = likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            likeList = likeRepository.findByPostId(postId.get());
        } else {
            likeList = likeRepository.findAll();
        }
        return likeList.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
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
