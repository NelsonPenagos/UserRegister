package com.company.register.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
public class UserDTO {

    //TODO: mover los mensajes a MessageKey
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    //TODO: mover las expresiones regulares al properties
    @Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$", message = "Password format is incorrect")
    private String password;

    private List<PhoneDTO> phones;


}
