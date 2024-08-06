package org.example.service;

import lombok.AllArgsConstructor;

import org.example.domain.User;
import org.example.exceptions.MyExceptions;
import org.example.repository.UserRepository;
import org.example.repository.interfaces.IUserRepository;
import org.example.service.interfaces.IUserService;
import org.example.validators.UserValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.bind.ValidationException;

@org.springframework.stereotype.Service(value = "userService")
@AllArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    private final IUserRepository userRepository;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder cryptPasswordEncoder;

    public Long logIn(String userName, String password) throws IllegalAccessException {
        User user = userRepository.getUserByUsername(userName);
        if (user == null) {
            throw new IllegalAccessException("The user with these credentials does not exist.");
        }
        if (!cryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new IllegalAccessException("Invalid password.");
        }
        return user.getId();
    }

    public void registerUser(User user) throws ValidationException {
        try {
            userValidator.validateUser(user);
            if (userRepository.getUserByUsername(user.getUsername()) != null) {
                throw new ValidationException("username is already taken!");
            }
            if (userRepository.getUserByEmail(user.getEmail()) != null) {
                throw new ValidationException("email is already taken!");
            }
            String password = cryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(password);
            userRepository.saveUser(user);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.getUserByUsername(username);
    }
}
