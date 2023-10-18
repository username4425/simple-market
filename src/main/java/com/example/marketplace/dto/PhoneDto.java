package com.example.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {
    private String manufacturer, name;
    @JsonProperty("seller_number")
    private Long sellerNumber;
    @JsonProperty("battery_size")
    private Integer batterySize;
    private Integer price;
    @JsonProperty("in_stock")
    private Integer inStock;
}
