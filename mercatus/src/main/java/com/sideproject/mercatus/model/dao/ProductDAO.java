package com.sideproject.mercatus.model.dao;

import com.sideproject.mercatus.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductDAO extends ListCrudRepository<Product, Long> {
}
