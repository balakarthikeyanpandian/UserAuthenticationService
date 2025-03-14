package com.example.userauthenticationservice.dtos;

import com.example.userauthenticationservice.models.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private Status status;
}
