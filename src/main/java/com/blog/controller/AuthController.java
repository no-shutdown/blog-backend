package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.dto.LoginRequest;
import com.blog.dto.LoginResponse;
import com.blog.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${blog.jwt.expiration}")
    private long expiration;

    @PostMapping("/api/admin/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getUsername());
        return ApiResponse.success(new LoginResponse(token, expiration));
    }
}
