package com.medical.reservation.controller;

import com.medical.reservation.dto.AuthResponse;
import com.medical.reservation.dto.LoginRequest;
import com.medical.reservation.dto.LoginResponse;
import com.medical.reservation.dto.RegisterRequest;
import com.medical.reservation.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new patient user")
    @PostMapping("/register")
    public AuthResponse registerPatient(@RequestBody RegisterRequest request) {
        return authService.registerPatient(request);
    }

    @Operation(summary = "Authenticate a user and return a JWT token")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}