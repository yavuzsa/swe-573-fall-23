package com.project.storyapp.services;

import com.project.storyapp.entities.Comment;
import com.project.storyapp.entities.Post;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.CommentRepository;
import com.project.storyapp.requests.CommentCreateRequest;
import com.project.storyapp.requests.CommentUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private PostService postService;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, userService, postService);
    }

    @Test
    void testGetAllComments_WithUserIdAndPostId_ReturnsFilteredComments() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findByUserIdAndPostId(userId, postId)).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getAllComments(Optional.of(userId), Optional.of(postId));

        // Assert
        assertEquals(expectedComments, result);
    }

    @Test
    void testGetAllComments_WithUserId_ReturnsFilteredComments() {
        // Arrange
        Long userId = 1L;
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findByUserId(userId)).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getAllComments(Optional.of(userId), Optional.empty());

        // Assert
        assertEquals(expectedComments, result);
    }

    @Test
    void testGetAllComments_WithPostId_ReturnsFilteredComments() {
        // Arrange
        Long postId = 2L;
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findByPostId(postId)).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getAllComments(Optional.empty(), Optional.of(postId));

        // Assert
        assertEquals(expectedComments, result);
    }

    @Test
    void testGetAllComments_WithoutUserIdAndPostId_ReturnsAllComments() {
        // Arrange
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getAllComments(Optional.empty(), Optional.empty());

        // Assert
        assertEquals(expectedComments, result);
    }

    @Test
    void testGetOneCommentById_WithValidId_ReturnsComment() {
        // Arrange
        Long commentId = 1L;
        Comment expectedComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        // Act
        Comment result = commentService.getOneCommentById(commentId);

        // Assert
        assertEquals(expectedComment, result);
    }

    @Test
    void testGetOneCommentById_WithInvalidId_ReturnsNull() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act
        Comment result = commentService.getOneCommentById(commentId);

        // Assert
        assertEquals(null, result);
    }

    @Test
    void testCreateOneComment_WithValidRequest_ReturnsCreatedComment() {
        // Arrange
        CommentCreateRequest request = new CommentCreateRequest();
        request.setUserId(1L);
        request.setPostId(2L);
        request.setText("Test comment");

        User user = new User();
        when(userService.getOneUserById(request.getUserId())).thenReturn(user);

        Post post = new Post();
        when(postService.getOnePostById(request.getPostId())).thenReturn(post);

        Comment expectedComment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(expectedComment);

        // Act
        Comment result = commentService.createOneComment(request);

        // Assert
        assertEquals(expectedComment, result);
    }

    @Test
    void testCreateOneComment_WithInvalidRequest_ReturnsNull() {
        // Arrange
        CommentCreateRequest request = new CommentCreateRequest();
        request.setUserId(1L);
        request.setPostId(2L);
        request.setText("Test comment");

        when(userService.getOneUserById(request.getUserId())).thenReturn(null);
        when(postService.getOnePostById(request.getPostId())).thenReturn(null);

        // Act
        Comment result = commentService.createOneComment(request);

        // Assert
        assertEquals(null, result);
        verify(commentRepository, never()).save(any(Comment.class));
    }


    @Test
    void testUpdateOneCommentById_WithInvalidId_ReturnsNull() {
        // Arrange
        Long commentId = 1L;
        CommentUpdateRequest request = new CommentUpdateRequest();
        request.setText("Updated comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act
        Comment result = commentService.updateOneCommentById(commentId, request);

        // Assert
        assertEquals(null, result);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testDeleteOneCommentById_DeletesComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        commentService.deleteOneCommentById(commentId);

        // Assert
        verify(commentRepository, times(1)).deleteById(commentId);
    }
}
