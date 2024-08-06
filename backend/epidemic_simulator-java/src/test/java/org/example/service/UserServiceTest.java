package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.User;
import org.example.repository.UserRepository;
import org.example.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.bind.ValidationException;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private BCryptPasswordEncoder cryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testRegisterUser() throws ValidationException {
        User user = new User("username", "email", "password", true, true);
        when(userRepository.getUserByUsername(anyString())).thenReturn(null);
        when(userRepository.getUserByEmail(anyString())).thenReturn(null);
        when(cryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.registerUser(user);

        verify(userRepository).saveUser(any(User.class));
    }

}
