package com.example.marketplace.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenFilter filter) throws Exception{
        http
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/client").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/client").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/client").hasRole("USER")
                        .requestMatchers("/client/cart/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/book/**", "/washing-machine/**", "/phone/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/book/**", "/washing-machine/**", "/phone/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/book/**", "/washing-machine/**", "/phone/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/book/**", "/washing-machine/**", "/phone/**").hasRole("SELLER")
                        .anyRequest().hasRole("ADMIN"))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
