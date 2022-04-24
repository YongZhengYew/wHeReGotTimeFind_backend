package com.infosys.service;

import com.infosys.model.Product;
import com.infosys.repository.ProductRepository;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends GenericModelService<Product, Integer, ProductRepository>{

    public JSONArray getByNameFuzzy(String productName) {
        JSONArray result = new JSONArray();
        repository.findByNameFuzzy(productName).stream()
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

    public JSONArray getByVendorId(Integer vendorId) {
        JSONArray result = new JSONArray();
        repository.findDistinctByReviewsVendorId(vendorId).stream()
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

    public boolean checkIfExists(String productName) {
        return repository.existsByName(productName);
    }
}
