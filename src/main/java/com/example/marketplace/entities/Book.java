package com.example.marketplace.entities;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@JsonView(Views.Any.class)
public class Book extends Product{

    public Book(){
        super(ProductType.BOOKS);
    }

    private String author, title;
}
