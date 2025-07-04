package com.example.user_service.service;

import org.springframework.stereotype.Service;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.dto.UserRequest;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;
 
    public User saveOrUpdate(String email, UserRequest req) {
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setName(req.getName());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
return userRepository.save(user);
    }
 
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
 
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }
}