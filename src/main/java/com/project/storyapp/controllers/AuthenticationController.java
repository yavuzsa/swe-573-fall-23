package com.project.storyapp.controllers;

import com.project.storyapp.entities.User;
import com.project.storyapp.requests.UserRequest;
import com.project.storyapp.responses.AuthenticationResponse;
import com.project.storyapp.security.JwtTokenProvider;
import com.project.storyapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {


    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                    UserService userService, PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody UserRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);
        User user = userService.getOneUserByUserName(loginRequest.getUserName());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setMessage("Bearer " + jwtToken);
        authenticationResponse.setUserId(user.getId());
        return authenticationResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequest registerRequest) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(userService.getOneUserByUserName(registerRequest.getUserName()) != null) {
            authenticationResponse.setMessage("Username Already Exists!");
            return new ResponseEntity<>(authenticationResponse, HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.createOneUser(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUserName(), registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        authenticationResponse.setMessage("Bearer " + jwtToken);
        authenticationResponse.setUserId(user.getId());
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }



}
