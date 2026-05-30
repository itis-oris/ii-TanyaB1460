package org.example.repository;

import org.example.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findByFilter(Long categoryId, Double minPrice, Double maxPrice);
}
