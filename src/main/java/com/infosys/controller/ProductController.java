package com.infosys.controller;

import com.infosys.service.ProductService;
import com.infosys.service.VendorService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/productName/{productName}")
    String getProductsByNameFuzzy(@PathVariable String productName) {
        return productService.getByNameFuzzy(productName).toString();
    }

    @GetMapping("/vendor/{vendorId}")
    String getProductsByVendorId(@PathVariable Integer vendorId) {
        return productService.getByVendorId(vendorId).toString();
    }
}
