package com.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingUpRequest {
    private String email;
    private String password;
}
