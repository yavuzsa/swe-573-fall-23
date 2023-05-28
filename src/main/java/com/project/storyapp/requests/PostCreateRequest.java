package com.project.storyapp.requests;

import com.project.storyapp.entities.Location;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class PostCreateRequest {

    Long id;
    String text;
    String title;
    Long userId;

    Date storyDate;
    Location location;
}
