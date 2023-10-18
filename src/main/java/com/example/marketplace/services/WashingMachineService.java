package com.example.marketplace.services;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.WashingMachineDto;
import com.example.marketplace.entities.Views;
import com.example.marketplace.entities.WashingMachine;
import com.example.marketplace.repositories.WashingMachineRepository;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WashingMachineService {
    private WashingMachineRepository washingMachineRepository;

    public WashingMachineService(WashingMachineRepository WashingMachineRepository) {
        this.washingMachineRepository = WashingMachineRepository;
    }

    public List<WashingMachine> getAllWashingMachines(){
        return washingMachineRepository.findAll();
    }

    public WashingMachine getWashingMachineById(Long id) throws AppException{
        return washingMachineRepository.findById(id).orElseThrow(() -> new AppException("error", "Washing machine with id %d doesn't exists".formatted(id), 404));
    }

    @Transactional
    public WashingMachine updateWashingMachine(Long id, WashingMachineDto newValue) throws AppException {
        if(washingMachineRepository.existsById(id)){
            WashingMachine washingMachine = washingMachineRepository.findById(id).get();
            if(newValue.getName() != null){
                washingMachine.setName(newValue.getName());
            }
            if(newValue.getDrumVolume() != null){
                washingMachine.setDrumVolume(newValue.getDrumVolume());
            }
            if(newValue.getManufacturer() != null){
                washingMachine.setManufacturer(newValue.getManufacturer());
            }
            if(newValue.getPrice() != null){
                washingMachine.setPrice(newValue.getPrice());
            }

            if(newValue.getSellerNumber() != null){
                washingMachine.setSellerNumber(newValue.getSellerNumber());
            }
            if(newValue.getInStock() != null){
                washingMachine.setInStock(newValue.getInStock());
            }
            return washingMachine;
        }
        throw new AppException("error", "WashingMachine with this id doesn't exists", 404);
    }

    @Transactional
    public WashingMachine createWashingMachine(WashingMachineDto washingMachineDto) throws AppException{
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.setName(washingMachineDto.getName());
        washingMachine.setPrice(washingMachineDto.getPrice());
        washingMachine.setManufacturer(washingMachineDto.getManufacturer());
        washingMachine.setDrumVolume(washingMachineDto.getDrumVolume());
        washingMachine.setSellerNumber(washingMachineDto.getSellerNumber());
        washingMachine.setInStock(washingMachineDto.getInStock());
        washingMachineRepository.save(washingMachine);
        return washingMachine;
    }

    @Transactional
    public WashingMachine deleteById(Long id) throws AppException{
        WashingMachine washingMachine = washingMachineRepository.findById(id).orElse(null);
        if(washingMachine != null){
            washingMachineRepository.deleteById(id);
            return washingMachine;
        }
        throw new AppException("error", "WashingMachine with this id doesn't exists", 404);
    }

    @Transactional
    public void deleteAll(){
        washingMachineRepository.deleteAll();
    }
}
