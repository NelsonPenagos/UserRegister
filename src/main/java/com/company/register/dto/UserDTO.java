package com.company.register.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
public class UserDTO {

    @NotEmpty(message = "{name.empty.message}")
    private String name;

    @Email(message = "{email.format.message}")
    @NotEmpty(message = "{email.empty.message}")
    private String email;

    @NotEmpty(message = "{password.empty.message}")
    @Size(min = 6, message = "{password.long.message}")
    private String password;

    private List<PhoneDTO> phones;


}
