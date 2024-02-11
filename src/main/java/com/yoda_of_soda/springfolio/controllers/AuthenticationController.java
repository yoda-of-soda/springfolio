package com.yoda_of_soda.springfolio.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yoda_of_soda.springfolio.models.GithubTokenCollection;
import com.yoda_of_soda.springfolio.models.GithubUser;
import com.yoda_of_soda.springfolio.models.GoogleUser;
import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.request.DecodeJWTRequest;
import com.yoda_of_soda.springfolio.request.DecodedJWTResponse;
import com.yoda_of_soda.springfolio.request.GoogleTokenResponse;
import com.yoda_of_soda.springfolio.request.LoginRequest;
import com.yoda_of_soda.springfolio.request.LoginResponse;
import com.yoda_of_soda.springfolio.services.AuthenticationService;
import com.yoda_of_soda.springfolio.services.GithubService;
import com.yoda_of_soda.springfolio.services.GoogleService;
import com.yoda_of_soda.springfolio.services.UserService;

import org.bouncycastle.openssl.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final GithubService githubService;
    private final GoogleService googleService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, GithubService githubService, UserService userService, GoogleService googleService) {
        this.authenticationService = authenticationService;
        this.githubService = githubService;
        this.userService = userService;
        this.googleService = googleService;
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DecodedJWTResponse("Could not parse JWT token"));
        }
    }

    @GetMapping("/signup/github")
    public RedirectView GithubSignupLogin() {
        return new RedirectView(githubService.GetOauthPage());
    }
    
    @GetMapping("/login/github")
    public ResponseEntity<LoginResponse> GithubCallback(@RequestParam String code) {
        GithubUser githubUser = githubService.LoginCallback(code);
        User user = userService.addUser(githubUser);
        try {
            String jwtToken = authenticationService.login(user);
            return ResponseEntity.ok(new LoginResponse(jwtToken, ""));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse("", e.getMessage()));
        }
    }

    @GetMapping("/signup/google")
    public RedirectView GoogleSignupLogin() {
        return new RedirectView(googleService.GetOauthPage());
    }

    @GetMapping("/login/google")
    public ResponseEntity<LoginResponse> GoogleCallback(@RequestParam String code) {
        GoogleUser googleUser = googleService.LoginCallback(code);
        User user = userService.addUser(googleUser);
        try {
            String jwtToken = authenticationService.login(user);
            return ResponseEntity.ok(new LoginResponse(jwtToken, ""));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse("", e.getMessage()));
        }
    }
}
