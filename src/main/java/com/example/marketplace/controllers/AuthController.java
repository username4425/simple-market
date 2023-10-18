package com.example.marketplace.controllers;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.ClientDto;
import com.example.marketplace.dto.SignupDto;
import com.example.marketplace.entities.Client;
import com.example.marketplace.services.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.hibernate.sql.Restriction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {

    @Value("${jwt-auth.server}")
    private String authServer;

    private ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/signup")
    @CrossOrigin("*")
    public ResponseEntity<?> signUp(@RequestBody ClientDto clientDto, @RequestParam String as) throws AppException {
        RestTemplate template = new RestTemplate();
        SignupDto signupDto = new SignupDto();
        signupDto.setPassword(clientDto.getPassword());
        signupDto.setUsername(clientDto.getUsername());
        HttpEntity<SignupDto> request = new HttpEntity<>(signupDto);
        ResponseEntity<String> response = template.exchange(authServer + "/auth/signup?as=" + as, HttpMethod.POST, request, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            Client client = clientService.createClient(clientDto);
            return ResponseEntity.ok().body(client);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<?> login(@RequestBody SignupDto signupDto, HttpServletResponse response) throws AppException{
        RestTemplate template = new RestTemplate();
        HttpEntity<SignupDto> request = new HttpEntity<>(signupDto);
        String token = template.postForObject(authServer + "/auth/login", request, String.class);
        Cookie authCookie = new Cookie("auth_cookie", token);
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        return ResponseEntity.ok().build();
    }
}
