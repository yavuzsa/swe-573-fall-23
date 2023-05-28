package com.project.storyapp.responses;

import com.project.storyapp.entities.Like;
import com.project.storyapp.entities.Location;
import com.project.storyapp.entities.Post;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class PostResponse {

    Long id;
    Long userId;
    String userName;
    String title;
    String text;

    Date createDate;
    Date storyDate;

    Location location;
    List<LikeResponse> likes;

    public PostResponse(Post entity, List<LikeResponse> likes) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUserName();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.likes = likes;
        this.createDate = entity.getCreateDate();
        this.storyDate = entity.getStoryDate();
        this.location = entity.getLocation();

    }
}
