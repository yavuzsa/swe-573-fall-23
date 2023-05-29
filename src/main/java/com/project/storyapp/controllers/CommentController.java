package com.project.storyapp.controllers;


import com.project.storyapp.entities.Comment;
import com.project.storyapp.requests.CommentCreateRequest;
import com.project.storyapp.requests.CommentUpdateRequest;
import com.project.storyapp.services.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return commentService.getAllComments(userId,postId);
    }

    @PostMapping
    public Comment createOneComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        return commentService.createOneComment(commentCreateRequest);
    }

    @GetMapping("/{commentId}")
    public Comment getOneComment(@PathVariable Long commentId) {
        return commentService.getOneCommentById(commentId);
    }

    @PutMapping("/{commentId}")
    public Comment updateOneComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return commentService.updateOneCommentById(commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteOneComment(@PathVariable Long commentId) {
        commentService.deleteOneCommentById(commentId);
    }
}
