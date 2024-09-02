package com.company.register.controller;

import com.company.register.dto.ErrorResponse;
import com.company.register.dto.UserDTO;
import com.company.register.dto.UserResponse;
import com.company.register.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "{\"mensaje\": \"Bienvenido al dashboard\"}";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

        if (result.hasErrors()) {
            List<ErrorResponse> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorResponse(Objects.requireNonNull(error.getDefaultMessage())))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        UserResponse userResponse = userService.registerUser(userDTO);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }
}
