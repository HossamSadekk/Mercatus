package com.sideproject.mercatus.service;

import com.sideproject.mercatus.model.Product;
import com.sideproject.mercatus.model.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductDAO productDAO;

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
}
