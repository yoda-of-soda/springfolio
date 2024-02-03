package com.yoda_of_soda.springfolio.request;

import java.util.HashMap;

public class DecodedJWTResponse {
    private HashMap<String, Object> headers;
    private HashMap<String, Object> data;
    private String signature;
    private String error;
    
    public DecodedJWTResponse(HashMap<String, Object> headers, HashMap<String, Object> data, String signature){
        this.headers = headers;
        this.data = data;
        this.signature = signature;
    }
    
    public DecodedJWTResponse(String error){
        this.error = error;
    }
    
    public HashMap<String, Object> getHeaders() {
        return headers;
    }
    
    public HashMap<String, Object> getData() {
        return data;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public String getError() {
        return error;
    }
}
