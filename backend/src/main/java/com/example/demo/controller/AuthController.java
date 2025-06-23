package com.example.demo.controller;

import com.example.demo.payload.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Qui va la logica di autenticazione (per ora facciamo finta vada bene)
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            return ResponseEntity.ok().body("{\"token\":\"fake-jwt-token\"}");
        } else {
            return ResponseEntity.status(401).body("{\"message\":\"Username o password errati\"}");
        }
    }
}
