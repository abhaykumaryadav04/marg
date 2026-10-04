package com.forensiq.marg.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.forensiq.marg.user.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Service 
public class JwtService {
private final SecretKey secretKey;
private final long expirationTime;
public JwtService(@Value("${jwt.secretkey}") String secret,@Value("${jwt.expiration}") long expire){

  this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  this.expirationTime=expire;
}
public String generateToken(User user){
    Date now=new Date();
    Date expiryDate=new Date(now.getTime()+expirationTime);
    return   Jwts.builder().subject(user.getId().toString())
                           .claim("email", user.getEmail())
                           .claim("role", user.getRole().toString())
                           .issuedAt(now)
                           .expiration(expiryDate)
                           .signWith(secretKey)
                           .compact();
}
public String generateRefreshToken(User user) {

    Date now = new Date();

    Date expiry = new Date(
            now.getTime() + (7L * 24 * 60 * 60 * 1000)
    );

    return Jwts.builder()
            .subject(user.getId().toString())
            .claim("type", "refresh")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
}
}
