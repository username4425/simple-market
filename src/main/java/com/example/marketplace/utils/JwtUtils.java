package com.example.marketplace.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtUtils {
    private static final String issuer = "auth app";

    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtUtils(
            @Value("${jwt-auth.secret}") String secret) {
        algorithm = Algorithm.HMAC512(secret);
        verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public boolean isValid(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTDecodeException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken retrieveUser(String token){
        try{
            DecodedJWT decodedJWT = verifier.verify(token);
            return new UsernamePasswordAuthenticationToken(
                    decodedJWT.getSubject(), null,
                    decodedJWT.getClaim("authorities").asList(String.class).stream().map(credential -> new SimpleGrantedAuthority("ROLE_" + credential)).toList());
        }catch(JWTDecodeException e){
            log.warn(e.getMessage());
            return null;
        }
    }
}