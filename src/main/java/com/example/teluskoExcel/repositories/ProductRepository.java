package com.example.teluskoExcel.repositories;

import com.example.teluskoExcel.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
