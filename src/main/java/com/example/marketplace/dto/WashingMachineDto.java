package com.example.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashingMachineDto {
    @JsonProperty("drum_volume")
    private Integer drumVolume;

    private Integer price;
    @JsonProperty("seller_number")
    private Long sellerNumber;

    private String manufacturer, name;
    @JsonProperty("in_stock")
    private Integer inStock;
}
