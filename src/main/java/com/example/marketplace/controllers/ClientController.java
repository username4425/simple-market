package com.example.marketplace.controllers;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.ClientDto;
import com.example.marketplace.entities.Client;
import com.example.marketplace.entities.Views;
import com.example.marketplace.services.ClientService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/client")
@Log4j2
@JsonView(Views.UserCart.class)
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public List<Client> getAllClients(){
        List<Client> clients = clientService.getAllClients();
        return clients;
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getClientById(@PathVariable Long id) throws AppException{
        Client client = clientService.getClientById(id);
        if(client != null) return ResponseEntity.ok().body(client);
        throw new AppException("error", "Client with id %d doesn't exists".formatted(id), 404);
    }

    @GetMapping
    @CrossOrigin("*")
    @ResponseStatus(HttpStatus.OK)
    public Client getCurrentClient(Principal principal) throws AppException{
        String username = principal.getName();
        return clientService.getClientByUsername(username);
    }

    @PatchMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) throws AppException{
        Client client = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok().body(client);
    }

    @PatchMapping()
    @CrossOrigin("*")
    @ResponseStatus(HttpStatus.OK)
    public Client updateCurrentClient(@RequestBody ClientDto clientDto, Principal principal) throws AppException{
        return clientService.updateClient(principal.getName(), clientDto);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) throws AppException{
        Client client = clientService.deleteById(id);
        return ResponseEntity.ok().body(client);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("*")
    public Client deleteCurrentClient(Principal principal) throws AppException{
        return clientService.deleteByUsername(principal.getName());
    }

    @DeleteMapping("/all")
    @CrossOrigin("*")
    public ResponseEntity<?> deleteAllClients(){
        clientService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart")
    @CrossOrigin("*")
    public ResponseEntity<?> addProductInCart( @RequestParam(name="product_id") Long productId, @RequestParam int count, Principal principal) throws AppException{
        clientService.addProductToCart(principal.getName(), productId, count);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{clientId}/cart")
    @CrossOrigin("*")
    public ResponseEntity<?> addProductInCart(@PathVariable Long clientId, @RequestParam(name="product_id") Long productId, @RequestParam int count) throws AppException{
        clientService.addProductToCart(clientId, productId, count);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart")
    @CrossOrigin("*")
    @JsonView(Views.UserCart.class)
    public ResponseEntity<?> getAllProductsInCart(Principal principal)throws AppException{
        return ResponseEntity.ok().body(clientService.getAllProductsInCart(principal.getName()));
    }

    @GetMapping("{clientId}/cart")
    @CrossOrigin("*")
    @JsonView(Views.UserCart.class)
    public ResponseEntity<?> getAllProductsInCartById(@PathVariable Long clientId)throws AppException{
        return ResponseEntity.ok().body(clientService.getAllProductsInCart(clientId));
    }

    @DeleteMapping("/cart")
    @CrossOrigin("*")
    public ResponseEntity<?> removeFromCart( @RequestParam(name="product_id") Long productId, Principal principal) throws AppException{
        clientService.deleteFromCart(principal.getName(), productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{clientId}/cart")
    @CrossOrigin("*")
    public ResponseEntity<?> removeFromCartById(@PathVariable Long clientId, @RequestParam(name="product_id") Long productId) throws AppException{
        clientService.deleteFromCart(clientId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/buy")
    @CrossOrigin("*")
    public ResponseEntity<?> buyProductsInCart(Principal principal) throws AppException{
        int totalPrice = clientService.buyProductsInCart(principal.getName());
        return ResponseEntity.ok().body("Successfully bought cart with total price %d.".formatted(totalPrice));
    }
}
