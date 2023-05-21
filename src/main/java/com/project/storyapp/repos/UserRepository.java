package com.project.storyapp.repos;

import com.project.storyapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByUserName(String userName);
}
