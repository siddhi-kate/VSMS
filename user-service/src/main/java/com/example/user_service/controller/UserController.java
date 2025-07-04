package com.example.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.user_service.dto.UserRequest;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import com.example.user_service.config.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
 
    private final UserService userService;
 
    @PostMapping("/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> saveOrUpdateProfile(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody UserRequest request) {
        String email = extractEmail(authHeader);
        return ResponseEntity.ok(userService.saveOrUpdate(email, request));
    }
 
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        String email = extractEmail(authHeader);
        System.out.println(email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }
 
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }
 
    private String extractEmail(String authHeader) {
        String token = authHeader.substring(7);
        return new JwtUtil().extractEmail(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

