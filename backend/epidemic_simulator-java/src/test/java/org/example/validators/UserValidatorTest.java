package org.example.validators;

import org.example.domain.User;
import org.example.validators.UserValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    void validateUser_ValidEmailAndPassword_DoesNotThrowException() {
        User user = new User("username", "valid.email@example.com", "Password1", true, true);
        assertDoesNotThrow(() -> userValidator.validateUser(user));
    }

    @Test
    void validateUser_InvalidEmail_ThrowsException() {
        User user = new User("username", "invalid-email", "Password1", true, true);
        Exception exception = assertThrows(Exception.class, () -> userValidator.validateUser(user));
        assertEquals("Email is not valid!", exception.getMessage());
    }

    @Test
    void validateUser_InvalidPassword_ThrowsException() {
        User user = new User("username", "valid.email@example.com", "password", true, true);
        Exception exception = assertThrows(Exception.class, () -> userValidator.validateUser(user));
        assertEquals("Password must contain at least 1 uppercase letter ,at least one number and has to be at least 8 characters!", exception.getMessage());
    }
}
