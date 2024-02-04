package com.yoda_of_soda.springfolio.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yoda_of_soda.springfolio.request.DecodeJWTRequest;
import com.yoda_of_soda.springfolio.request.DecodedJWTResponse;
import com.yoda_of_soda.springfolio.request.LoginRequest;
import com.yoda_of_soda.springfolio.request.LoginResponse;
import com.yoda_of_soda.springfolio.services.AuthenticationService;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/hello")
    public String getHello() {
        return "Helloooo!!";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String jwtToken = authenticationService.login(request);
            return ResponseEntity.ok(new LoginResponse(jwtToken, ""));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse("", e.getMessage()));
        } catch (PasswordException e) {
            return ResponseEntity.badRequest().body(new LoginResponse("", e.getMessage()));
        }
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
