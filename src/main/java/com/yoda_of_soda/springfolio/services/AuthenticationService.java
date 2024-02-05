package com.yoda_of_soda.springfolio.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PasswordException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.request.DecodedJWTResponse;
import com.yoda_of_soda.springfolio.request.LoginRequest;

@Service
public class AuthenticationService {

    private final UserService userService;
    private static ObjectMapper objectMapper;
    private final JwtService jwtService;

    public AuthenticationService(UserService userService, JwtService jwtService){
        objectMapper = new ObjectMapper();
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public DecodedJWTResponse DecodeJWT(String jwt) throws IllegalAccessException{
        if (!jwt.matches("[^\\.]+\\.[^\\.]+\\.[^\\.]+")){
            throw new IllegalAccessException("JWT must contain 2 separators(\".\")");
        }
        String[] parts = jwt.split("\\.", 3);
        try {
            HashMap<String, Object> headers = jsonToHashMap(new String(Base64.decodeBase64(parts[0])));
            HashMap<String, Object> data =  jsonToHashMap(new String(Base64.decodeBase64(parts[1])));
            String signature = parts[2];
            return new DecodedJWTResponse(headers, data, signature);
        } catch (IOException e) {
            return new DecodedJWTResponse(e.toString());
        }
    }

    public String login(LoginRequest request) throws UsernameNotFoundException, PasswordException {
        User user = userService.getUserByUsername(request.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("Username '" + request.getUsername() + "' not found");
        }
        if(!BCrypt.checkpw(request.getPassword(), user.getPassword())){
            throw new PasswordException("Password and/or username is incorrect");
        }
        return jwtService.generateToken(user);
    }

    public String login(User user) throws UsernameNotFoundException {
        User existingUser = userService.getUserByUsername(user.getUsername());
        if(existingUser == null){
            throw new UsernameNotFoundException("Username '" + user.getUsername() + "' not found");
        }
        return jwtService.generateToken(user);
    }

    public static HashMap<String, Object> jsonToHashMap(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
    }
}