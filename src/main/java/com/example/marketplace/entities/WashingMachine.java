package com.example.marketplace.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="washing_machine")
@Data
@AllArgsConstructor
@JsonView(Views.Any.class)
public class WashingMachine extends Product {
    private String manufacturer, name;

    @Column(name = "drum_volume")
    @JsonProperty("drum_volume")
    private int drumVolume;

    public WashingMachine(){
        super(ProductType.HOME_APPLIANCES);
    }
}
