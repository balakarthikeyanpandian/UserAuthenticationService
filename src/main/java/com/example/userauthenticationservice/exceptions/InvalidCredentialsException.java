package com.example.userauthenticationservice.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
