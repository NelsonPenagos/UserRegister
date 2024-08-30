package com.company.register.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class JwtTokenGenerator {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String createJwtToken(String userId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userId)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }
}
