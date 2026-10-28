package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.AuthResponse;
import com.Api.OqaJanaFezHoje.dtos.LoginRequest;
import com.Api.OqaJanaFezHoje.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}