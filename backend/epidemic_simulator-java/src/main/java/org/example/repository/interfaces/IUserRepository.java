package org.example.repository.interfaces;

import org.example.domain.User;

import java.util.List;

public interface IUserRepository {

    List<User> getAllUsers();
    void saveUser(User user);
    void updateUser(User newUser, User oldUser);
    User getUserByUsername(String username);
    User getUserById(Long id);
    User getUserByEmail(String email);

}
