package com.project.storyapp.entities;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;
    String title;

    @Lob
    @Column(columnDefinition = "text")
    String text;


    Date createDate;

    Date storyDate;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="location_id")
    Location location;
}
