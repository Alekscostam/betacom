package com.betacom.app.controller;

import com.betacom.app.dto.JwtAuthenticationResponse;
import com.betacom.app.dto.UserLoginRequest;
import com.betacom.app.dto.UserRegisterRequest;
import com.betacom.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegisterRequest request) {
        service.register(request.getLogin(), request.getPassword());
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody  UserLoginRequest request) {
        return ResponseEntity.ok(service.login(request.getLogin(), request.getPassword()));

    }


}
