package com.project.storyapp.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="comment")
@Data
public class Comment {

    @Id
    Long id;
    Long postId;
    Long userId;
    @Lob
    @Column(columnDefinition = "text")
    String text;
}
