package com.infosys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.model.Product;
import com.infosys.model.projection.ProductView;
import com.infosys.repository.ProductRepository;
import com.infosys.service.DTO.FullReview;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends GenericModelService<Product, Integer, ProductRepository>{
    @Autowired
    private ObjectMapper objectMapper;

    public JSONArray getByNameFuzzy(String productName) {
        JSONArray result = new JSONArray();
        for (ProductView product : repository.findByNameFuzzy(productName)) {
            JSONObject fr = null;
            try {
                fr = new JSONObject(objectMapper.writeValueAsString(product));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            result.put(fr);
        }
        return result;
    }

    public JSONArray getByVendorId(Integer vendorId) {
        JSONArray result = new JSONArray();
        for (ProductView product : repository.findDistinctByReviewsVendorId(vendorId)) {
            JSONObject fr = null;
            try {
                fr = new JSONObject(objectMapper.writeValueAsString(product));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            result.put(fr);
        }
        return result;
    }
}
