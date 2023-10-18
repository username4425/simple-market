package com.example.marketplace.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="phone")
@Data
@AllArgsConstructor
@JsonView(Views.Any.class)
public class Phone extends Product {

    private String manufacturer, name;

    @Column(name="battery_size")
    @JsonProperty("battery_size")
    private Integer batterySize;

    public Phone(){
        super(ProductType.PHONES);
    }
}
