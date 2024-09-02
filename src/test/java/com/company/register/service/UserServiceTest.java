package com.company.register.service;

import com.company.register.constants.MessageKeys;
import com.company.register.dto.UserDTO;
import com.company.register.dto.UserResponse;
import com.company.register.exception.EmailAlreadyRegisteredException;
import com.company.register.exception.EmailValidationException;
import com.company.register.exception.PasswordValidationException;
import com.company.register.model.User;
import com.company.register.repository.UserRepository;
import com.company.register.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private ValidationService validationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageService msj;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("StrongPassword123");
        userDTO.setPhones(Collections.emptyList());

        when(validationService.isValidEmail(anyString())).thenReturn(true);
        when(validationService.isValidPassword(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        //when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        UserResponse userResponse = userService.registerUser(userDTO);

        assertNotNull(userResponse.getId());
        assertNotNull(userResponse.getCreated());
        assertNotNull(userResponse.getModified());
        assertNotNull(userResponse.getLastLogin());
        assertEquals("token", userResponse.getToken());
        assertTrue(userResponse.getIsActive());
    }

    @Test
    public void testRegisterUser_EmailInvalid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("invalid-email");

        when(validationService.isValidEmail(anyString())).thenReturn(false);
        when(msj.getMessage(MessageKeys.EMAIL_FORMAT_MESSAGE)).thenReturn("Invalid email format");

        EmailValidationException exception = assertThrows(EmailValidationException.class, () -> {
            userService.registerUser(userDTO);
        });

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    public void testRegisterUser_EmailAlreadyRegistered() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john.doe@example.com");

        when(validationService.isValidEmail(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(msj.getMessage(MessageKeys.REGISTER_FAIL)).thenReturn("Email already registered");

        EmailAlreadyRegisteredException exception = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            userService.registerUser(userDTO);
        });

        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    public void testRegisterUser_PasswordInvalid() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("weakpassword");

        when(validationService.isValidEmail(anyString())).thenReturn(true);
        when(validationService.isValidPassword(anyString())).thenReturn(false);
        when(msj.getMessage(MessageKeys.PASSWORD_FORMAT_MESSAGE)).thenReturn("Invalid password format");

        PasswordValidationException exception = assertThrows(PasswordValidationException.class, () -> {
            userService.registerUser(userDTO);
        });

        assertEquals("Invalid password format", exception.getMessage());
    }
}