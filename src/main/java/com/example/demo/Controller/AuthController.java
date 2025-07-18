package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AppUser;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.PasswordResetService;

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
    @PostMapping("/login")
public AuthenticationResponse authenticate(@RequestBody LoginRequest request) {
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

    @Autowired
    private PasswordResetService passwordResetService;
    @PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody java.util.Map<String, String> body) {
    String email = body.get("email");

    try {
        passwordResetService.initiateReset(email);
    } catch (RuntimeException e) {
        // Don't reveal whether user exists
        System.out.println("📨 Password reset attempted for: " + email + " | " + e.getMessage());
    }

    return ResponseEntity.ok("If the email is registered, a reset link has been sent.");
}

    @PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody java.util.Map<String, String> body) {
    String token = body.get("token");
    String newPassword = body.get("newPassword");

    try {
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful.");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Reset failed: " + e.getMessage());
    }
}


    
}