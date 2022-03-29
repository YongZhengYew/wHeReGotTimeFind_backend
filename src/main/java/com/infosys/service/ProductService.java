package com.infosys.service;

import com.infosys.model.Product;
import com.infosys.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends GenericModelService<Product, Integer, ProductRepository>{

}
