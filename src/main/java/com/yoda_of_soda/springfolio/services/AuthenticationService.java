package com.yoda_of_soda.springfolio.services;

import java.io.IOException;
import java.util.HashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoda_of_soda.springfolio.request.DecodedJWTResponse;

@Service
public class AuthenticationService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static HashMap<String, Object> jsonToHashMap(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {});
    }
}