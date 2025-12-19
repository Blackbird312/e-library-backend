package com.novelis.elibrary.controller;

import com.novelis.elibrary.dto.login.AuthResponse;
import com.novelis.elibrary.dto.login.LoginRequest;
import com.novelis.elibrary.dto.user.RegisterRequest;
import com.novelis.elibrary.entity.User;
import com.novelis.elibrary.service.JwtService;
import com.novelis.elibrary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        String token = jwtService.generateToken(auth);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(token)
                        .type("Bearer")
                        .expiresInSeconds(jwtService.expiresInSeconds())
                        .build()
        );
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(req.password());

        userService.create(user);
    }
}
