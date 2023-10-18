package com.example.marketplace.controllers;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.PhoneDto;
import com.example.marketplace.entities.Client;
import com.example.marketplace.entities.Phone;
import com.example.marketplace.entities.Views;
import com.example.marketplace.services.ClientService;
import com.example.marketplace.services.PhoneService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private PhoneService phoneService;
    private ClientService clientService;

    public PhoneController(PhoneService phoneService, ClientService clientService) {
        this.phoneService = phoneService;
        this.clientService = clientService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public List<Phone> getAllPhones(){
        List<Phone> phones = phoneService.getAllPhones();
        return phones;
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> getPhoneById(@PathVariable Long id) throws AppException {
        Phone phone = phoneService.getPhoneById(id);
        if(phone != null) return ResponseEntity.ok().body(phone);
        throw new AppException("error", "Phone with id %d doesn't exists".formatted(id), 404);
    }

    @PostMapping
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> createPhone(@RequestBody PhoneDto phoneDto, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        phoneDto.setSellerNumber(currentSeller.getId());
        Phone phone = phoneService.createPhone(phoneDto);
        return ResponseEntity.ok().body(phone);
    }

    @PatchMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> updatePhone(@PathVariable Long id, @RequestBody PhoneDto phoneDto, Principal principal) throws AppException{
        Phone phone = phoneService.getPhoneById(id);
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        if(!currentSeller.getId().equals(phone.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        phone = phoneService.updatePhone(id, phoneDto);
        return ResponseEntity.ok().body(phone);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> deletePhone(@PathVariable Long id, Principal principal) throws AppException{
        Phone phone = phoneService.getPhoneById(id);
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        if(!currentSeller.getId().equals(phone.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        phone = phoneService.deleteById(id);
        return ResponseEntity.ok().body(phone);
    }
}
