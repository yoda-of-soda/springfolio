package com.yoda_of_soda.springfolio.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims; 
import io.jsonwebtoken.SignatureAlgorithm; 
import io.jsonwebtoken.io.Decoders; 
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.yoda_of_soda.springfolio.models.User;

import java.security.Key; 
import java.util.Date; 
import java.util.HashMap; 
import java.util.Map; 
import java.util.function.Function; 

@Service
public class JwtService {

    // @Value("jwt.secret")
    public static String SECRET = "220085b93074bf28f8893979e8e6ee1220085b93074bf28f8893979e8e6ee1"; 

    public String generateToken(User user) { 
        Map<String, Object> claims = new HashMap<String,Object>();
        claims.put("user_id", user.getId());
        return Jwts.builder() 
                .setClaims(claims) 
                .setSubject(user.getUsername()) 
                .setIssuedAt(new Date(System.currentTimeMillis())) 
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) 
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); 
    } 

    private Key getSignKey() { 
        byte[] keyBytes= Decoders.BASE64.decode(SECRET); 
        return Keys.hmacShaKeyFor(keyBytes); 
    }

    public String extractUsername(String token) { 
        return extractClaim(token, Claims::getSubject); 
    } 
  
    public Date extractExpiration(String token) { 
        return extractClaim(token, Claims::getExpiration); 
    } 
  
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
        final Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody(); 
        return claimsResolver.apply(claims); 
    } 
  
    private Boolean isTokenExpired(String token) { 
        return extractExpiration(token).before(new Date()); 
    } 
  
    public Boolean validateToken(String token, UserDetails userDetails) { 
        final String username = extractUsername(token); 
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
    } 
}
