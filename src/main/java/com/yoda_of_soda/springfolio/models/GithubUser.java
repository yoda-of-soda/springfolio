package com.yoda_of_soda.springfolio.models;

import java.math.BigInteger;

public class GithubUser {
    private BigInteger id;
    // login = JSON key for username
    private String login;
    private String name;
    private String email;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
