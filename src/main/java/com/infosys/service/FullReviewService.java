package com.infosys.service;

import com.infosys.model.projection.ProductView;
import com.infosys.model.projection.ReviewView;
import com.infosys.model.projection.VendorView;
import com.infosys.repository.*;
import com.infosys.service.DTO.FullReview;
import com.infosys.utils.ObjectMapperUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FullReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    public JSONArray getFullReviewsByUsernames(String[] usernames) {
        JSONArray result = new JSONArray();
        Arrays.stream(usernames)
                .map(reviewRepository::findReviewViewByUserName)
                .flatMap(Collection::stream)
                .map(FullReview::new)
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

    public JSONArray getFullReviewsByTagIds(Integer[] tagIds) {
        JSONArray userReviewArray = new JSONArray();
        List<ReviewView> reviewViews = reviewRepository.findDistinctByTagsIdIn(tagIds);
        reviewViews.stream()
                .map(FullReview::new)
                .map(objectMapperUtil::getJSONObject)
                .forEach(userReviewArray::put);
        return userReviewArray;
    }

    public JSONArray getFullReviewsByProductNameFuzzy(String productName) {
        Stream<Integer> productIdsFuzzy = productRepository.findByNameFuzzy(productName)
                .stream().map(ProductView::getId);

        Stream<ReviewView> reviewViews = productIdsFuzzy
                .map(reviewRepository::findReviewViewByProductId)
                .flatMap(List::stream);

        JSONArray result = new JSONArray();
        reviewViews
                .map(FullReview::new)
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

    public JSONArray getFullReviewsByVendorNameFuzzy(String vendorName) {
        Stream<Integer> vendorIdsFuzzy = vendorRepository.findVendorViewByNameFuzzy(vendorName)
                .stream().map(VendorView::getId);

        Stream<ReviewView> reviewViews = vendorIdsFuzzy
                .map(reviewRepository::findReviewViewByVendorId)
                .flatMap(List::stream);

        JSONArray result = new JSONArray();
        reviewViews
                .map(FullReview::new)
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }
}
