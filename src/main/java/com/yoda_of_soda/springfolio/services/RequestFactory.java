package com.yoda_of_soda.springfolio.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class RequestFactory {
    public static <T> HttpEntity<T> create(T body, HttpHeaders headers){
        if(headers == null){
            headers = new HttpHeaders();
        }
        return new HttpEntity<T>(body, headers);
    }
}
