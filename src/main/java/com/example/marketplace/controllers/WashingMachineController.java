package com.example.marketplace.controllers;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.WashingMachineDto;
import com.example.marketplace.entities.Client;
import com.example.marketplace.entities.Views;
import com.example.marketplace.entities.WashingMachine;
import com.example.marketplace.services.ClientService;
import com.example.marketplace.services.WashingMachineService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/washing-machine")
public class WashingMachineController {
    private WashingMachineService washingMachineService;
    private ClientService clientService;

    public WashingMachineController(WashingMachineService washingMachineService, ClientService clientService) {
        this.washingMachineService = washingMachineService;
        this.clientService = clientService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public List<WashingMachine> getAllWashingMachines(){
        List<WashingMachine> washingMachines = washingMachineService.getAllWashingMachines();
        return washingMachines;
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> getWashingMachineById(@PathVariable Long id) throws AppException {
        WashingMachine washingMachine = washingMachineService.getWashingMachineById(id);
        throw new AppException("error", "WashingMachine with id %d doesn't exists".formatted(id), 404);
    }

    @PostMapping
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> createWashingMachine(@RequestBody WashingMachineDto washingMachineDto, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        washingMachineDto.setSellerNumber(currentSeller.getId());
        WashingMachine washingMachine = washingMachineService.createWashingMachine(washingMachineDto);
        return ResponseEntity.ok().body(washingMachine);
    }

    @PatchMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> updateWashingMachine(@PathVariable Long id, @RequestBody WashingMachineDto washingMachineDto, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        WashingMachine washingMachine = washingMachineService.getWashingMachineById(id);
        if(!currentSeller.getId().equals(washingMachine.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        washingMachine = washingMachineService.updateWashingMachine(id, washingMachineDto);
        return ResponseEntity.ok().body(washingMachine);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> deleteWashingMachine(@PathVariable Long id, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        WashingMachine washingMachine = washingMachineService.getWashingMachineById(id);
        if(!currentSeller.getId().equals(washingMachine.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        washingMachine = washingMachineService.deleteById(id);
        return ResponseEntity.ok().body(washingMachine);
    }
}
