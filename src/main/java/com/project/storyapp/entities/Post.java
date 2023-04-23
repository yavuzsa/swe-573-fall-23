package com.project.storyapp.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="post")
@Data
public class Post {

    @Id
    Long id;
    Long userId;
    String title;
    @Column(columnDefinition = "text")
    String text;

}
