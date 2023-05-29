package com.project.storyapp.services;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Location;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.PostRepository;
import com.project.storyapp.requests.PostCreateRequest;
import com.project.storyapp.requests.PostUpdateRequest;
import com.project.storyapp.responses.LikeResponse;
import com.project.storyapp.responses.PostResponse;
import com.project.storyapp.services.LikeService;
import com.project.storyapp.services.LocationService;
import com.project.storyapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private LikeService likeService;

    @Mock
    private LocationService locationService;

    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, userService, likeService, locationService);
    }

    @Test
    void testGetAllPostsByUserIdWithInvalidUserId() {
        Long userId = 1L;

        when(postRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        List<PostResponse> result = postService.getAllPosts(Optional.of(userId));

        assertEquals(0, result.size());

        verify(postRepository, times(1)).findByUserId(userId);
        verify(likeService, never()).getAllLikes(any(), any());
    }

    @Test
    void testGetAllPostsByNoUserId() {
        when(postRepository.findAll()).thenReturn(new ArrayList<>());

        List<PostResponse> result = postService.getAllPosts(Optional.empty());

        assertEquals(0, result.size());

        verify(postRepository, times(1)).findAll();
        verify(likeService, never()).getAllLikes(any(), any());
    }

    @Test
    void testGetOnePostByIdWithValidId() {
        Long postId = 1L;
        Post expectedPost = new Post();

        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));

        Post result = postService.getOnePostById(postId);

        assertEquals(expectedPost, result);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void testGetOnePostByIdWithInvalidId() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Post result = postService.getOnePostById(postId);

        assertNull(result);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void testCreateOnePostWithValidData() {
        Long userId = 1L;
        Long postId = 2L;
        User user = new User();
        user.setId(userId);

        PostCreateRequest createRequest = new PostCreateRequest();
        createRequest.setUserId(userId);
        createRequest.setId(postId);
        createRequest.setText("Sample text");
        createRequest.setTitle("Sample title");
        createRequest.setStoryDate(new Date());
        createRequest.setLocation(new Location());

        when(userService.getOneUserById(userId)).thenReturn(user);

        Post expectedPost = new Post();
        expectedPost.setId(postId);
        expectedPost.setText("Sample text");
        expectedPost.setTitle("Sample title");
        expectedPost.setCreateDate(new Date());
        expectedPost.setStoryDate(createRequest.getStoryDate());
        expectedPost.setUser(user);
        expectedPost.setLocation(createRequest.getLocation());

        when(postRepository.save(any(Post.class))).thenReturn(expectedPost);

        Post result = postService.createOnePost(createRequest);

        assertEquals(expectedPost, result);
        verify(userService, times(1)).getOneUserById(userId);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testCreateOnePostWithInvalidUser() {
        Long userId = 1L;
        PostCreateRequest createRequest = new PostCreateRequest();
        createRequest.setUserId(userId);

        when(userService.getOneUserById(userId)).thenReturn(null);

        Post result = postService.createOnePost(createRequest);

        assertNull(result);
        verify(userService, times(1)).getOneUserById(userId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testUpdateOnePostByIdWithValidId() {
        Long postId = 1L;
        String newText = "Updated text";
        String newTitle = "Updated title";

        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setText(newText);
        updateRequest.setTitle(newTitle);

        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setText("Old text");
        existingPost.setTitle("Old title");

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post result = postService.updateOnePostById(postId, updateRequest);

        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals(newText, result.getText());
        assertEquals(newTitle, result.getTitle());

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testUpdateOnePostByIdWithInvalidId() {
        Long postId = 1L;
        PostUpdateRequest updateRequest = new PostUpdateRequest();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Post result = postService.updateOnePostById(postId, updateRequest);

        assertNull(result);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testDeleteOnePostById() {
        Long postId = 1L;

        postService.deleteOnePostById(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }
}
