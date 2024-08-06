package org.example.validators;

import lombok.AllArgsConstructor;
import org.example.domain.User;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@AllArgsConstructor
@Component
public class UserValidator {

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

    public void validateUser(User user) throws Exception {
        if(!isValidEmail(user.getEmail())){
            throw new Exception("Email is not valid!");
        }
        if(!isValidPassword(user.getPassword())){
            throw new Exception("Password must contain at least 1 uppercase letter ,at least one number and has to be at least 8 characters!");
        }
    }

}
