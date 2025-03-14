package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.*;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SingUpRequest signUpRequest){

        List<String> roleList = new ArrayList<>();
        roleList.add("STUDENT");
        roleList.add("TRIAL_USER");

        try{

            User user = authService.signup(signUpRequest.getEmail(),signUpRequest.getPassword(),roleList);
            return new ResponseEntity<>(from(user),HttpStatus.ACCEPTED);

        }catch (UserPresentAlreadyException exception){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        }catch (RoleDoesNotExistException exception2){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

        }


    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest){
        try{
            Pair<User,String> response = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

            String message = response.b;

            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE,message);

            return new ResponseEntity<>(from(response.a),headers,HttpStatus.ACCEPTED);
        }catch (InvalidCredentialsException exception){
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }catch (UserDoesNotExistException exception){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<TokenResponseDto> validateToken(@RequestBody RequestTokenDto requestTokenDto){

        TokenResponseDto tokenResponseDto = new TokenResponseDto();

        try{
            authService.validateToken(requestTokenDto.getToken(), requestTokenDto.getUserId());

        }catch(UnAuthorizedUserException exception){
            tokenResponseDto.setStatus(false);
            tokenResponseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(tokenResponseDto,HttpStatus.UNAUTHORIZED);
        }

        tokenResponseDto.setStatus(true);
        tokenResponseDto.setMessage("Valid Token");
        return new ResponseEntity<>(tokenResponseDto,HttpStatus.ACCEPTED);
    }

    private UserDto from(User user){

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setId(user.getId());

        return userDto;
    }

}
