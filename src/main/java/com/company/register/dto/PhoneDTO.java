package com.company.register.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneDTO {
    @NotEmpty(message = "Phone number cannot be empty")
    private String number;

    @NotEmpty(message = "City code cannot be empty")
    private String citycode;

    @NotEmpty(message = "Country code cannot be empty")
    private String contrycode;
}
