package com.yoda_of_soda.springfolio.request;

public class LoginResponse {
    private String token;
    private String error;
    
    public LoginResponse(String token, String error) {
        if(token != "") this.token = token;
        if(error != "") this.error = error;
    }

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }

}
