package org.example.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.example.controllers.requests.LoginRequest;
import org.example.service.UserService;
import org.example.service.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final IUserService service;

    @PostMapping
    public ResponseEntity<?> logIn(@RequestBody LoginRequest logInRequest) {
        try {
            Long id = service.logIn(logInRequest.getUsername(), logInRequest.getPassword());
            //String token = generateToken(logInRequest.getUsername());
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256,  Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .compact();
    }

}
