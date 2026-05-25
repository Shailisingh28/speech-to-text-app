package com.sttapp.Security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String Secret_key = "mysecretkeymysecretkeymysecretkey12";

    private final Key key = Keys.hmacShaKeyFor(Secret_key.getBytes());

    public String generateJWT(String email) {
        return Jwts.builder().setSubject(email).setExpiration(new Date(System.currentTimeMillis()
                + 1000 * 60 * 60 * 24)).setIssuedAt(new Date()).signWith(key, SignatureAlgorithm.HS256).compact();
    }
}
