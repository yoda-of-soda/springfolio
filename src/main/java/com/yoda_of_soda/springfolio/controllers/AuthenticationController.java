package com.yoda_of_soda.springfolio.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoda_of_soda.springfolio.request.DecodeJWTRequest;
import com.yoda_of_soda.springfolio.request.DecodedJWTResponse;
import com.yoda_of_soda.springfolio.services.AuthenticationService;
import com.yoda_of_soda.springfolio.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @GetMapping("/hello")
    public String getHello() {
        return "Helloooo!!";
    }
    
    @PostMapping("/jwt/decode")
    public ResponseEntity<DecodedJWTResponse> decodeJWT(@RequestBody DecodeJWTRequest entity) {
        try {
            DecodedJWTResponse response = authenticationService.DecodeJWT(entity.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("IERROR: " + e.toString());
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new DecodedJWTResponse("Could not parse JWT token"));
        }
    }
    
}
