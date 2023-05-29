package com.project.storyapp.services;

import com.project.storyapp.entities.User;
import com.project.storyapp.repos.UserRepository;
import com.project.storyapp.security.JwtUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }


    @Test
    public void testLoadUserByIdWithExistingId() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        JwtUserDetails userDetails = (JwtUserDetails) userDetailsService.loadUserById(userId);

        // Assert that the returned UserDetails object is not null
        assertNotNull(userDetails);
        assertEquals(user.getId(), userDetails.getId());
    }

}
