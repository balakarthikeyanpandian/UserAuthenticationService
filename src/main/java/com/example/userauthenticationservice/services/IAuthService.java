package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.InvalidCredentialsException;
import com.example.userauthenticationservice.exceptions.RoleDoesNotExistException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.exceptions.UserPresentAlreadyException;
import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface IAuthService {
    User signup(String email, String password, List<String> roleList) throws UserPresentAlreadyException, RoleDoesNotExistException;

    Pair<User,String> login(String email, String password) throws UserDoesNotExistException, InvalidCredentialsException;

}
