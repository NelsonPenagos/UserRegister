package com.company.register.dto;

import com.company.register.constants.PatternKeys;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternKeys.DATE_FORMAT)
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternKeys.DATE_FORMAT)
    private LocalDateTime modified;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternKeys.DATE_FORMAT)
    private LocalDateTime lastLogin;

    private String token;
    private Boolean isActive;
}
