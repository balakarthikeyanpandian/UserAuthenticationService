package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.InvalidCredentialsException;
import com.example.userauthenticationservice.exceptions.RoleDoesNotExistException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.exceptions.UserPresentAlreadyException;
import com.example.userauthenticationservice.models.Role;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.RoleRepo;
import com.example.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signup(String email, String password, List<String> rolesList) throws UserPresentAlreadyException,RoleDoesNotExistException {

        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserPresentAlreadyException("Try Login");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        ArrayList<Role> roles = new ArrayList<>();


        for(String s : rolesList){
            Optional<Role> optionalRole = roleRepo.findByValue(s);
            if(optionalRole.isPresent()){
                roles.add(optionalRole.get());
            }else{
                throw new RoleDoesNotExistException(s+" roles does not exit");
            }
        }

        user.setRoleList(roles);

        return userRepo.save(user);

    }

    public Pair<User,String> login(String email, String password) throws UserDoesNotExistException, InvalidCredentialsException{

        Optional<User> optionalUser = userRepo.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException(email+" does not exist");
        }

//        if(!password.equals(optionalUser.get().getPassword())){
//            throw new InvalidCredentialsException("The email & password does not match");
//        }

        if(!bCryptPasswordEncoder.matches(password,optionalUser.get().getPassword()))
        {
            throw new InvalidCredentialsException("The email & password does not match");
        }

        String message  = "Logged in successfully";

        //Generate JWT tokens
//        String jsonMessage = "{\n" +
//                "\"name\":\"bala\"\n" +
//                "\"message\":\""+message+"\"}";
//
//        byte[] convertedMessage = jsonMessage.getBytes(StandardCharsets.UTF_8);

//        String finalMessage = Jwts.builder().content(convertedMessage).compact();

        Map<String,Object> tokenValue = new HashMap<>();

        Long timeInMilli = System.currentTimeMillis();
        Long expTimeInMilli = timeInMilli +  (60*60*24*30*1000L);

        tokenValue.put("iat",timeInMilli);
        tokenValue.put("exp",expTimeInMilli);
        tokenValue.put("userId",optionalUser.get().getId());
        tokenValue.put("source","API");

        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
        SecretKey secretKey = macAlgorithm.key().build();

        String token = Jwts.builder().claims(tokenValue).signWith(secretKey).compact();

        Pair<User,String> pair = new Pair<>(optionalUser.get(),token);

        return pair;

    }
}
