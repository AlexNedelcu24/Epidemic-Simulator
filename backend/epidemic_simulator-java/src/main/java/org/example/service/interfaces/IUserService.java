package org.example.service.interfaces;

import org.example.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.xml.bind.ValidationException;

public interface IUserService {

    Long logIn(String userName, String password) throws IllegalAccessException;
    void registerUser(User user) throws ValidationException;
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
