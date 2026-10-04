package com.forensiq.marg.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.forensiq.marg.auth.dto.AuthResponse;
import com.forensiq.marg.auth.dto.LoginRequest;
import com.forensiq.marg.auth.dto.RefreshTokenRequest;
import com.forensiq.marg.auth.dto.RegisterRequest;
import com.forensiq.marg.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/auth")
public class AuthController {
    @Autowired 
    private AuthService authService;

    @PostMapping ("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid  RegisterRequest request){
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
    @PostMapping ("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody  LoginRequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.login(request));
    }
    @GetMapping ("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication){
        String userId=authentication.getName();
        return ResponseEntity.ok(authService.getCurrentUser(userId));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh( @Valid @RequestBody RefreshTokenRequest request) {
    return ResponseEntity.ok( authService.refreshAccessToken( request.refreshToken() ));
    }
   
    @PostMapping("/logout")
     public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request) {
    authService.logout(request.refreshToken());
    return ResponseEntity.ok("Logout successful");
    }

}
