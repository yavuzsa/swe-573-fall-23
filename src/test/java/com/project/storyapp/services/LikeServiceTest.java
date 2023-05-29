package com.project.storyapp.services;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.LikeRepository;
import com.project.storyapp.requests.LikeCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    private LikeService likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        likeService = new LikeService(likeRepository, userService, postService);
    }

    @Test
    void testGetOneLikeById() {
        Long likeId = 1L;
        Like expectedLike = new Like();

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(expectedLike));

        Like result = likeService.getOneLikeById(likeId);

        assertEquals(expectedLike, result);
        verify(likeRepository, times(1)).findById(likeId);
    }

    @Test
    void testGetOneLikeByIdWithInvalidId() {
        Long likeId = 1L;

        when(likeRepository.findById(likeId)).thenReturn(Optional.empty());

        Like result = likeService.getOneLikeById(likeId);

        assertNull(result);
        verify(likeRepository, times(1)).findById(likeId);
    }

    @Test
    void testCreateOneLikeWithValidUserAndPost() {
        Long userId = 1L;
        Long postId = 1L;
        Long likeId = 1L;

        LikeCreateRequest likeCreateRequest = new LikeCreateRequest();
        likeCreateRequest.setUserId(userId);
        likeCreateRequest.setPostId(postId);
        likeCreateRequest.setId(likeId);

        User user = new User();
        Post post = new Post();
        Like savedLike = new Like();

        when(userService.getOneUserById(userId)).thenReturn(user);
        when(postService.getOnePostById(postId)).thenReturn(post);
        when(likeRepository.save(any(Like.class))).thenReturn(savedLike);

        Like result = likeService.createOneLike(likeCreateRequest);

        assertEquals(savedLike, result);
        verify(userService, times(1)).getOneUserById(userId);
        verify(postService, times(1)).getOnePostById(postId);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void testCreateOneLikeWithInvalidUser() {
        Long userId = 1L;
        Long postId = 1L;
        Long likeId = 1L;

        LikeCreateRequest likeCreateRequest = new LikeCreateRequest();
        likeCreateRequest.setUserId(userId);
        likeCreateRequest.setPostId(postId);
        likeCreateRequest.setId(likeId);

        Post post = new Post();

        when(userService.getOneUserById(userId)).thenReturn(null);
        when(postService.getOnePostById(postId)).thenReturn(post);

        Like result = likeService.createOneLike(likeCreateRequest);

        assertNull(result);
        verify(userService, times(1)).getOneUserById(userId);
        verify(postService, times(1)).getOnePostById(postId);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void testCreateOneLikeWithInvalidPost() {
        Long userId = 1L;
        Long postId = 1L;
        Long likeId = 1L;

        LikeCreateRequest likeCreateRequest = new LikeCreateRequest();
        likeCreateRequest.setUserId(userId);
        likeCreateRequest.setPostId(postId);
        likeCreateRequest.setId(likeId);

        User user = new User();

        when(userService.getOneUserById(userId)).thenReturn(user);
        when(postService.getOnePostById(postId)).thenReturn(null);

        Like result = likeService.createOneLike(likeCreateRequest);

        assertNull(result);
        verify(userService, times(1)).getOneUserById(userId);
        verify(postService, times(1)).getOnePostById(postId);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void testDeleteOneLikeById() {
        Long likeId = 1L;

        likeService.deleteOneLikeById(likeId);

        verify(likeRepository, times(1)).deleteById(likeId);
    }
}
