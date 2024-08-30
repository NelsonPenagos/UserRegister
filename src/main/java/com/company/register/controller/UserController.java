package com.company.register.controller;

import com.company.register.dto.ErrorResponseDTO;
import com.company.register.dto.UserDTO;
import com.company.register.dto.UserResponseDTO;
import com.company.register.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

        if (result.hasErrors()) {
            List<ErrorResponseDTO> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorResponseDTO(Objects.requireNonNull(error.getDefaultMessage())))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            UserResponseDTO userResponse = userService.registerUser(userDTO);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
