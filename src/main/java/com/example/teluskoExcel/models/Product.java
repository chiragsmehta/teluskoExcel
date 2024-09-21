package com.example.teluskoExcel.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store product data as a JSON string to handle unknown schema
    @Lob
    private String data;

}
