package com.project.storyapp.services;

import com.project.storyapp.entities.User;
import com.project.storyapp.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testGetAllUsers() {
        // Prepare test data
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        when(userRepository.findAll()).thenReturn(userList);

        // Call the method to be tested
        List<User> result = userService.getAllUsers();

        // Verify the result
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateOneUser() {
        // Prepare test data
        User newUser = new User();
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Call the method to be tested
        User result = userService.createOneUser(newUser);

        // Verify the result
        assertNotNull(result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testGetOneUserByIdWithExistingId() {
        // Prepare test data
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the method to be tested
        User result = userService.getOneUserById(userId);

        // Verify the result
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetOneUserByIdWithNonExistingId() {
        // Prepare test data
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method to be tested
        User result = userService.getOneUserById(userId);

        // Verify the result
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateOneUserWithExistingId() {
        // Prepare test data
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setPassword("newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Call the method to be tested
        User result = userService.updateOneUser(userId, newUser);

        // Verify the result
        assertNotNull(result);
        assertEquals(newUser.getUserName(), result.getUserName());
        assertEquals(newUser.getPassword(), result.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateOneUserWithNonExistingId() {
        // Prepare test data
        Long userId = 999L;
        User newUser = new User();
        newUser.setUserName("newUser");
        newUser.setPassword("newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the method to be tested
        User result = userService.updateOneUser(userId, newUser);

        // Verify the result
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteById() {
        // Prepare test data
        Long userId = 1L;

        // Call the method to be tested
        userService.deleteById(userId);

        // Verify the method invocation
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testGetOneUserByUserName() {
        // Prepare test data
        String userName = "john123";
        User user = new User();
        user.setUserName(userName);
        when(userRepository.findByUserName(userName)).thenReturn(user);

        // Call the method to be tested
        User result = userService.getOneUserByUserName(userName);

        // Verify the result
        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        verify(userRepository, times(1)).findByUserName(userName);
    }
}
