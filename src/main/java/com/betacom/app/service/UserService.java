package com.betacom.app.service;


import com.betacom.app.dto.JwtAuthenticationResponse;
import com.betacom.app.entity.User;
import com.betacom.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository repository;

    public void register(String login, String password) {
        if (repository.findByLogin(login).isPresent())
            throw new RuntimeException("Login already exists");
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
    }

    public User getUserById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public JwtAuthenticationResponse login(String login, String password) {
        User user = repository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        return new JwtAuthenticationResponse(jwtService.generateToken(user.getId()));
    }

}

