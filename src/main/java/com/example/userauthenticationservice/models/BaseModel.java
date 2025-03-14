package com.example.userauthenticationservice.models;

import java.util.Date;

public abstract class BaseModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Status status;
}
