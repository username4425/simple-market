package com.example.marketplace.security;

import com.example.marketplace.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

@Component
@Log4j2
public class TokenFilter extends OncePerRequestFilter {
    JwtUtils jwtUtils;

    public TokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getCookies() != null){
            Cookie authCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equalsIgnoreCase("auth_cookie")).findFirst().orElse(null);
            if(authCookie != null){
                String token = authCookie.getValue();
                UsernamePasswordAuthenticationToken authToken = jwtUtils.retrieveUser(token);
                if(authToken != null){
                    log.info("user is authorized successfully ");
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else log.error("Cookie auth_cookie doesn't valid");
            }else log.error("No auth_cookie is present");
        }else{
            log.error("No cookies provided");
        }
        filterChain.doFilter(request, response);
    }
}
