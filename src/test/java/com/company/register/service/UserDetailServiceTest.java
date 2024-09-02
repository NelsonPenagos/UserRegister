package com.company.register.service;

import com.company.register.model.User;
import com.company.register.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // Act
        UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");

        // Assert
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername("unknown@example.com");
        });
    }
}