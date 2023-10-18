package com.example.marketplace.services;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.PhoneDto;
import com.example.marketplace.entities.Phone;
import com.example.marketplace.entities.Views;
import com.example.marketplace.repositories.PhoneRepository;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {
    private PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository PhoneRepository) {
        this.phoneRepository = PhoneRepository;
    }

    public List<Phone> getAllPhones(){
        return phoneRepository.findAll();
    }

    public Phone getPhoneById(Long id) throws AppException{
        return phoneRepository.findById(id).orElseThrow(() -> new AppException("error", "Phone with id %d doesn't exists".formatted(id), 404));
    }

    @Transactional
    public Phone updatePhone(Long id, PhoneDto newValue) throws AppException {
        if(phoneRepository.existsById(id)){
            Phone phone = phoneRepository.findById(id).get();
            if(newValue.getSellerNumber() != null){
                phone.setSellerNumber(newValue.getSellerNumber());
            }
            if(newValue.getName() != null){
                phone.setName(newValue.getName());
            }

            if(newValue.getManufacturer() != null){
                phone.setManufacturer(newValue.getManufacturer());
            }
            if(newValue.getBatterySize() != null){
                phone.setBatterySize(newValue.getBatterySize());
            }
            if(newValue.getPrice() != null){
                phone.setPrice(newValue.getPrice());
            }
            if(newValue.getInStock() != null){
                phone.setInStock(newValue.getInStock());
            }
            return phone;
        }
        throw new AppException("error", "Phone with this id doesn't exists", 404);
    }

    @Transactional
    public Phone createPhone(PhoneDto phoneDto) throws AppException{
        Phone phone = new Phone();
        phone.setName(phoneDto.getName());
        phone.setPrice(phoneDto.getPrice());
        phone.setManufacturer(phoneDto.getManufacturer());
        phone.setBatterySize(phoneDto.getBatterySize());
        phone.setSellerNumber(phoneDto.getSellerNumber());
        phone.setInStock(phoneDto.getInStock());
        phoneRepository.save(phone);
        return phone;
    }

    @Transactional
    public Phone deleteById(Long id) throws AppException{
        Phone phone = phoneRepository.findById(id).orElse(null);
        if(phone != null){
            phoneRepository.deleteById(id);
            return phone;
        }
        throw new AppException("error", "Phone with this id doesn't exists", 404);
    }

    @Transactional
    public void deleteAll(){
        phoneRepository.deleteAll();
    }
}
