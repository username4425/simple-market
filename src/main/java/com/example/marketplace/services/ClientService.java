package com.example.marketplace.services;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.ClientDto;
import com.example.marketplace.entities.Cart;
import com.example.marketplace.entities.Client;
import com.example.marketplace.entities.Product;
import com.example.marketplace.entities.Views;
import com.example.marketplace.repositories.CartRepository;
import com.example.marketplace.repositories.ClientRepository;
import com.example.marketplace.repositories.ProductRepository;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ClientService {
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private PasswordEncoder encoder;

    public ClientService(ClientRepository clientRepository, ProductRepository productRepository, CartRepository cartRepository, PasswordEncoder encoder) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.encoder = encoder;
    }

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    public Client getClientById(Long clientId) throws AppException{
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new AppException("error", "Client with id %d doesn't exists".formatted(clientId), 404));
        return client;
    }

    @Transactional
    public Client updateClient(String username, ClientDto newValue) throws AppException {
        Client client = getClientByUsername(username);
        return updateClient(client, newValue);
    }


    @Transactional
    public Client updateClient(Client client, ClientDto newValue) throws AppException {
        if (newValue.getUsername() != null && !newValue.getUsername().equals(client.getUsername())) {
            if (!clientRepository.existsByUsername(newValue.getUsername()))
                client.setUsername(newValue.getUsername());
            else
                throw new AppException("error", "Client with username %s already exists".formatted(newValue.getUsername()), 400);
        }
        if (newValue.getName() != null && !newValue.getName().equals(client.getName())) {
            client.setName(newValue.getName());

            if (newValue.getEmail() != null && !newValue.getEmail().equals(client.getEmail())) {
                if (!clientRepository.existsByEmail(newValue.getEmail()))
                    client.setEmail(newValue.getEmail());
                else throw new AppException("error", "Email %s is already taken".formatted(newValue.getEmail()), 400);
            }
        }
        return client;
    }

    @Transactional
    public Client updateClient(Long id, ClientDto newValue) throws AppException{
        Client client = getClientById(id);
        return updateClient(client, newValue);
    }

    @Transactional
    public Client createClient(ClientDto clientDto) throws AppException {
        if(clientRepository.existsByEmail(clientDto.getEmail())) throw new AppException("error", "Email %s is already taken".formatted(clientDto.getEmail()), 400);
        if(clientRepository.existsByUsername(clientDto.getUsername())) throw new AppException("error", "Client with username %d already exists!".formatted(clientDto.getUsername()), 400);
        Client client = new Client();
        client.setUsername(clientDto.getUsername());
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        clientRepository.save(client);
        Cart cart = new Cart();
        cartRepository.save(cart);
        client.setCart(cart);
        return client;
    }

    @Transactional
    public Client deleteById(Long id) throws AppException{
        Client client = clientRepository.findById(id).orElse(null);
        if(client != null){
            clientRepository.deleteById(id);
            return client;
        }
        throw new AppException("error", "Client with this id doesn't exists", 404);
    }

    @Transactional
    public Client deleteByUsername(String username) throws AppException{
        Client client = getClientByUsername(username);
        clientRepository.deleteById(client.getId());
        return client;
    }

    @Transactional
    public void deleteAll(){
        clientRepository.deleteAll();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void addProductToCart(String username, Long productId,  int count) throws AppException{
        Client client = getClientByUsername(username);
        Product product = productRepository.findById(productId).orElseThrow( () -> new AppException("error", "Product with id %d doesn't exists".formatted(productId), 404));
        Cart cart = client.getCart();
        if(product.getInStock() < count) throw new AppException("error", "Not enough products", 400);
        cart.getProductsInCart().put(product.getId(), count);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void addProductToCart(Long clientId, Long productId,  int count) throws AppException{
        Client client = getClientById(clientId);
        Product product = productRepository.findById(productId).orElseThrow( () -> new AppException("error", "Product with id %d doesn't exists".formatted(productId), 404));
        Cart cart = client.getCart();
        if(product.getInStock() < count) throw new AppException("error", "Not enough products", 400);
        cart.getProductsInCart().put(product.getId(), count);
    }

    @Transactional
    public void deleteFromCart(String username, Long productId) throws AppException{
        Client client = getClientByUsername(username);
        Cart cart= client.getCart();
        if(cart.getProductsInCart().containsKey(productId)){
            cart.getProductsInCart().remove(productId);
        }else{
            throw new AppException("error", "No product with id %d in cart.".formatted(productId), 400);
        }
    }

    @Transactional
    public void deleteFromCart(Long clientId, Long productId) throws AppException{
        Client client = getClientById(clientId);
        Cart cart= client.getCart();
        if(cart.getProductsInCart().containsKey(productId)){
            cart.getProductsInCart().remove(productId);
        }else{
            throw new AppException("error", "No product with id %d in cart.".formatted(productId), 400);
        }
    }

    public List<Product> getAllProductsInCart(String username) throws AppException{
        Client client = getClientByUsername(username);
        Cart cart = client.getCart();
        Map<Long, Integer> cartMap = cart.getProductsInCart();
        List<Product> productsInCart = productRepository.getAllByIdIn(cart.getProductsInCart().keySet());
        for(Product p: productsInCart){
            p.setOffered(cartMap.get(p.getId()));
        }
        return productsInCart;
    }

    public List<Product> getAllProductsInCart(Long clientId) throws AppException{
        Client client = getClientById(clientId);
        Cart cart = client.getCart();
        Map<Long, Integer> cartMap = cart.getProductsInCart();
        List<Product> productsInCart = productRepository.getAllByIdIn(cart.getProductsInCart().keySet());
        for(Product p: productsInCart){
            p.setOffered(cartMap.get(p.getId()));
        }
        return productsInCart;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int buyProductsInCart(String username)throws AppException{
        Client client = getClientByUsername(username);
        Cart cart = client.getCart();
        int totalPrice = 0;
        List<Product> purchasedProducts = productRepository.getAllByIdIn(cart.getProductsInCart().keySet());
        for(Product product: purchasedProducts){
            int count = cart.getProductsInCart().get(product.getId());
            if(count > product.getInStock()){
                throw new AppException("error", "Not enough units in stock to purchase your cart", 400);
            }
            totalPrice += product.getPrice();
            product.setInStock(product.getInStock() - count);
            cart.getProductsInCart().remove(product.getId());
        }
        return totalPrice;
    }

    public Client getClientByUsername(String username) throws AppException{
        return clientRepository.findByUsername(username).orElseThrow(() -> new AppException("error", "Client with username %s doesn't exists".formatted(username), 404));
    }
}
