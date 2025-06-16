package com.example.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AppUser;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.RegisterRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/req")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public AuthenticationResponse register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(request.getUsername());

        return new AuthenticationResponse(token);
    }
    @PostMapping("/authenticate")
public AuthenticationResponse authenticate(@RequestBody RegisterRequest request) {
    System.out.println("🔐 Attempting login for: " + request.getUsername());

    AppUser user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                System.out.println("❌ Username not found");
                return new RuntimeException("Invalid username or password");
            });

    System.out.println("🧠 Fetched user: " + user.getUsername());
    System.out.println("🔒 Input password: " + request.getPassword());
    System.out.println("🔐 Stored password hash: " + user.getPassword());

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        System.out.println("❌ Passwords do NOT match");
        throw new RuntimeException("Invalid username or password");
    }

    System.out.println("✅ Password matches! Generating token...");
    String token = jwtUtil.generateToken(user.getUsername());
    return new AuthenticationResponse(token);
}

    /* 
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody RegisterRequest request) {
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthenticationResponse(token);
    }*/
}