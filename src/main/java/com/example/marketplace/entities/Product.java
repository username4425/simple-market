package com.example.marketplace.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Any.class)
    protected Long id;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    @JsonProperty("product_type")
    @JsonView(Views.Any.class)
    protected ProductType productType;

    @Column(name = "in_stock")
    @JsonView({Views.ProductDescription.class})
    @JsonProperty("in_stock")
    protected int inStock;

    @Transient
    @JsonView({Views.UserCart.class})
    protected int offered;

    @Column(name="seller_number")
    @JsonProperty("seller_number")
    private Long sellerNumber;
    private Integer price;

    Product(ProductType productType){
        this.productType = productType;
    }
}
