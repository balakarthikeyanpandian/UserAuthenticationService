package com.example.userauthenticationservice.exceptions;

public class UnAuthorizedUserException extends Exception{
    public UnAuthorizedUserException(String message){
        super(message);
    }
}
