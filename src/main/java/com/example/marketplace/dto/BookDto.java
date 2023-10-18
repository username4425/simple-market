package com.example.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String author, title;
    @JsonProperty("seller_number")
    private Long sellerNumber;

    private Integer price;

    @JsonProperty("in_stock")
    private Integer inStock;
}
