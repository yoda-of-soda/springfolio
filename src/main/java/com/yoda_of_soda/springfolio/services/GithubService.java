package com.yoda_of_soda.springfolio.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yoda_of_soda.springfolio.enums.OauthProvider;
import com.yoda_of_soda.springfolio.enums.Role;
import com.yoda_of_soda.springfolio.models.GithubEmail;
import com.yoda_of_soda.springfolio.models.GithubTokenCollection;
import com.yoda_of_soda.springfolio.models.GithubUser;
import com.yoda_of_soda.springfolio.models.User;

@Service
public class GithubService {
    private final String baseUrl;
    private HttpHeaders headers;
    RestTemplate restTemplate;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String loginRedirectURI;

    public GithubService(){
        baseUrl = "https://api.github.com/";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
        this.restTemplate = new RestTemplate();
    }

    public String GetOauthPage(){
        return "http://www.github.com/login/oauth/authorize?client_id="+clientId;
    }

    public GithubUser LoginCallback(String code){
        HttpEntity<Void> request = RequestFactory.create(null, headers);
        String url = String.format("https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s", 
        clientId, clientSecret, code, loginRedirectURI);
        
        ResponseEntity<GithubTokenCollection> response = restTemplate.postForEntity(url, request, GithubTokenCollection.class);
        GithubTokenCollection tokens = response.getBody();
        headers.setBearerAuth(tokens.getAccess_token());
        return GetUser();
    }

    private GithubUser GetUser(){
        String url = baseUrl + "user";
        HttpEntity<GithubUser> request = RequestFactory.create(null, headers);

        ResponseEntity<GithubUser> response = restTemplate.exchange(url, HttpMethod.GET, request, GithubUser.class);
        GithubUser user = response.getBody();
        if(user.getEmail() == null){
            user.setEmail(GetUserEmail());
        }
        return user;
    }

    private String GetUserEmail(){
        String url = baseUrl + "user/emails";
        HttpEntity<GithubUser> request = RequestFactory.create(null, headers);
        ResponseEntity<GithubEmail[]> response = restTemplate.exchange(url, HttpMethod.GET, request, GithubEmail[].class);
        GithubEmail[] emails = response.getBody();
        return emails[0].getEmail();
    }

    // Not using automapper/ModelMapper due to too many custom mappings
    public static User ConvertGithubUserToDomainUser(GithubUser githubUser){
        User user = new User();
        user.setEmail(githubUser.getEmail());
        user.setProvider(OauthProvider.GITHUB);
        user.setExternalId(githubUser.getId());
        user.setRole(Role.USER);
        user.setUsername(githubUser.getLogin());
        return user;
    }
}
