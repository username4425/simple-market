package com.example.marketplace.repositories;

import com.example.marketplace.entities.WashingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long> {
}
