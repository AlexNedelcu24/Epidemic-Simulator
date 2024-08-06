package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.controllers.requests.RegisterRequest;
import org.example.domain.User;
import org.example.service.UserService;
import org.example.service.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RequestMapping("/api/register")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RegisterController {

    private final IUserService service;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        try{
            service.registerUser(new User(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword(), false, false));
            return ResponseEntity.ok("ok!");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
