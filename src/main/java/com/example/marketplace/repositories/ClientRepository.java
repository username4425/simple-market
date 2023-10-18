package com.example.marketplace.repositories;

import com.example.marketplace.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<Client> findByUsername(String username);
}
