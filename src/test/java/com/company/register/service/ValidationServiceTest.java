package com.company.register.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationService = new ValidationService();

        org.springframework.test.util.ReflectionTestUtils.setField(validationService, "passwordPattern", "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$");
        org.springframework.test.util.ReflectionTestUtils.setField(validationService, "emailPattern", "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    @Test
    void testValidPassword() {
        String validPassword = "Valid123";
        assertTrue(validationService.isValidPassword(validPassword));
    }

    @Test
    void testInvalidPassword() {
        String invalidPassword = "invalid";
        assertFalse(validationService.isValidPassword(invalidPassword));
    }

    @Test
    void testValidEmail() {
        String validEmail = "test@example.com";
        assertTrue(validationService.isValidEmail(validEmail));
    }

    @Test
    void testInvalidEmail() {
        String invalidEmail = "invalid-email";
        assertFalse(validationService.isValidEmail(invalidEmail));
    }

    @Test
    void testNullPassword() {
        assertFalse(validationService.isValidPassword(null));
    }

    @Test
    void testEmptyPassword() {
        assertFalse(validationService.isValidPassword(""));
    }

    @Test
    void testNullEmail() {
        assertFalse(validationService.isValidEmail(null));
    }

    @Test
    void testEmptyEmail() {
        assertFalse(validationService.isValidEmail(""));
    }

}