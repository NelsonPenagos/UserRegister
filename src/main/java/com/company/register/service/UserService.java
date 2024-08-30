package com.company.register.service;

import com.company.register.constants.MessageKeys;
import com.company.register.dto.PhoneDTO;
import com.company.register.dto.UserDTO;
import com.company.register.dto.UserResponseDTO;
import com.company.register.model.Phone;
import com.company.register.model.User;
import com.company.register.repository.UserRepository;
import com.company.register.security.JwtTokenGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService msj;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO registerUser(UserDTO userDTO) {
        validateEmail(userDTO.getEmail());
        User user = createUserFromDTO(userDTO);
        user.setToken(generateToken(user.getId()));
        User savedUser = userRepository.save(user);
        return mapToUserResponseDTO(savedUser);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(msj.getMessage(MessageKeys.REGISTER_SUCCESS));
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

    private String generateToken(UUID userId) {
        return JwtTokenGenerator.createJwtToken(userId.toString());
    }

    private UserResponseDTO mapToUserResponseDTO(User savedUser) {
        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .lastLogin(savedUser.getLastLogin())
                .token(savedUser.getToken())
                .isActive(savedUser.getIsActive())
                .build();
    }

}
