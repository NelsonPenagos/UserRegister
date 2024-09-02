package com.company.register.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneDTO {
    @NotEmpty(message = "{phone.empty.message}")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "{phone.long.message}")
    private String number;

    @NotEmpty(message = "{city.empty.message}")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "{city.long.message}")
    private String citycode;

    @NotEmpty(message = "{country.empty.message}")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "{country.long.message}")
    private String contrycode;
}
