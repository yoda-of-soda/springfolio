package com.yoda_of_soda.springfolio.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yoda_of_soda.springfolio.enums.OauthProvider;
import com.yoda_of_soda.springfolio.enums.Role;
import com.yoda_of_soda.springfolio.models.GoogleUser;
import com.yoda_of_soda.springfolio.models.User;
import com.yoda_of_soda.springfolio.request.GoogleTokenResponse;

@Service
public class GoogleService {
    private String authorizationRequestUrl;
    RestTemplate restTemplate;

    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.client.secret}")
    private String clientSecret;
    @Value("${google.redirect_uri}")
    private String loginRedirectURI;
    private final String oauthApiUrl = "https://oauth2.googleapis.com/token";

    public GoogleTokenResponse DebugMe(){
        GoogleTokenResponse r = new GoogleTokenResponse();
        r.setAccess_token(clientId);
        r.setRefresh_token(clientSecret);
        r.setScope(loginRedirectURI);
        return r;
    }
    public GoogleService() {
        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter());
    }
    
    private String getStateValue() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes).replace("=", "");
    }
    
    public String GetOauthPage() {
        if(authorizationRequestUrl == null) {
            String scope = "https%3A//www.googleapis.com/auth/userinfo.email";
            authorizationRequestUrl =  String.format(
                "%s?client_id=%s&redirect_uri=%s&state=%s&scope=%s&include_granted_scopes=true&access_type=offline&response_type=code",
                "https://accounts.google.com/o/oauth2/v2/auth", clientId, loginRedirectURI, getStateValue(), scope);
        }
        return authorizationRequestUrl;
    }

    // Not using automapper/ModelMapper due to too many custom mappings
    public static User ConvertGoogleUserToDomainUser(GoogleUser googleUser){
        User user = new User();
        user.setExternalId(new BigInteger(googleUser.getId()));
        user.setEmail(googleUser.getEmail());
        user.setUsername(googleUser.getEmail());
        user.setProvider(OauthProvider.GOOGLE);
        user.setRole(Role.USER);
        return user;
    }

    public GoogleUser LoginCallback(String code){
        GoogleTokenResponse tokens = GetTokensFromGoogle(code);
        GoogleUser user = GetUserEmail(tokens.getAccess_token());
        return user;
    }

    private GoogleTokenResponse GetTokensFromGoogle(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format("client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s", 
        clientId, clientSecret, code, loginRedirectURI);
        
        HttpEntity<String> request = RequestFactory.create(requestBody, headers);
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(oauthApiUrl, request, GoogleTokenResponse.class);
        GoogleTokenResponse responseBody = response.getBody();
        GetUserEmail(responseBody.getAccess_token());
        return responseBody;
    }

    private GoogleUser GetUserEmail(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = RequestFactory.create(null, headers);
        ResponseEntity<GoogleUser> response = restTemplate.exchange("https://www.googleapis.com/oauth2/v1/userinfo", HttpMethod.GET, request, GoogleUser.class);
        return response.getBody();
    }
}
