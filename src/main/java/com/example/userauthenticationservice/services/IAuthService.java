package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.*;
import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface IAuthService {
    User signup(String email, String password, List<String> roleList) throws UserPresentAlreadyException, RoleDoesNotExistException;

    Pair<User,String> login(String email, String password) throws UserDoesNotExistException, InvalidCredentialsException;

    boolean validateToken(String token, Long userId) throws UnAuthorizedUserException;
}
