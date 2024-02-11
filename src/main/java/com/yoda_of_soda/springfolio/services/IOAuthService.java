package com.yoda_of_soda.springfolio.services;

import com.yoda_of_soda.springfolio.models.User;

public interface IOAuthService {
    public String GetOauthPage();
    public User LoginCallback(String code);
}
