package com.example.marketplace.services;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.entities.Cart;
import com.example.marketplace.entities.Product;
import com.example.marketplace.entities.ProductType;
import com.example.marketplace.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
