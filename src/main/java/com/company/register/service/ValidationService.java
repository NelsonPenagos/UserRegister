package com.company.register.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    @Value("${regex.password}")
    private String passwordPattern;

    @Value("${regex.email}")
    private String emailPattern;

    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.matches(passwordPattern);
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(emailPattern);
    }
}
