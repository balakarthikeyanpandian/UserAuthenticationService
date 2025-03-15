package com.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponseDto {
    private Boolean status;
    private String message;
}
