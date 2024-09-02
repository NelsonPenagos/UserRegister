package com.company.register.service;

import com.company.register.constants.MessageKeys;
import com.company.register.dto.PhoneDTO;
import com.company.register.dto.UserDTO;
import com.company.register.dto.UserResponse;
import com.company.register.exception.EmailAlreadyRegisteredException;
import com.company.register.exception.EmailValidationException;
import com.company.register.exception.PasswordValidationException;
import com.company.register.model.Phone;
import com.company.register.model.User;
import com.company.register.repository.UserRepository;
import com.company.register.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService msj;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(UserDTO userDTO) {
        validateEmail(userDTO.getEmail());
        validatePassword(userDTO.getPassword());
        User user = createUserFromDTO(userDTO);
        user.setToken(generateToken(userDTO.getEmail()));
        User savedUser = userRepository.save(user);
        return mapToUserResponseDTO(savedUser);
    }

    private void validateEmail(String email) {
        if (!validationService.isValidEmail(email)) {
            throw new EmailValidationException(msj.getMessage(MessageKeys.EMAIL_FORMAT_MESSAGE));
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(msj.getMessage(MessageKeys.REGISTER_FAIL));
        }
    }

    private void validatePassword(String password) {
        if (!validationService.isValidPassword(password)) {
            throw new PasswordValidationException(msj.getMessage(MessageKeys.PASSWORD_FORMAT_MESSAGE));
        }
    }

    private User createUserFromDTO(UserDTO userDTO) {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setIsActive(true);
        user.setPhones(mapPhones(userDTO.getPhones(), user));
        return user;
    }

    private List<Phone> mapPhones(List<PhoneDTO> phoneDTOs, User user) {
        return phoneDTOs.stream().map(phoneDTO -> {
            Phone phone = new Phone();
            phone.setNumber(phoneDTO.getNumber());
            phone.setCitycode(phoneDTO.getCitycode());
            phone.setContrycode(phoneDTO.getContrycode());
            phone.setUser(user);
            return phone;
        }).collect(Collectors.toList());
    }

    private String generateToken(String name) {
        return jwtUtil.generateToken(name);
    }

    private UserResponse mapToUserResponseDTO(User savedUser) {
        return UserResponse.builder()
                .id(savedUser.getId())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .lastLogin(savedUser.getLastLogin())
                .token(savedUser.getToken())
                .isActive(savedUser.getIsActive())
                .build();
    }

}
