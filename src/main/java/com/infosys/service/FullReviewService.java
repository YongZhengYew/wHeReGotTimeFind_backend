package com.infosys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.model.projection.ReviewView;
import com.infosys.repository.ReviewRepository;
import com.infosys.repository.TagRepository;
import com.infosys.repository.UserRepository;
import com.infosys.repository.VendorRepository;
import com.infosys.service.DTO.FullReview;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private ObjectMapper objectMapper;

    public JSONArray getFullReviewsByUsernames(String[] usernames) {
        JSONArray result = new JSONArray();
        for (String username : usernames) {
            JSONObject fullReviewsPerUser = new JSONObject();
            JSONArray userReviewArray = new JSONArray();
            List<ReviewView> ls = reviewRepository.findReviewViewByUserName(username);
            for (ReviewView rv : ls) {
                JSONObject fr = null;
                try {
                    fr = new JSONObject(objectMapper.writeValueAsString(new FullReview(rv)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                userReviewArray.put(fr);
            }
            fullReviewsPerUser.put("full_review", userReviewArray);
            result.put(fullReviewsPerUser);
        }
        return result;
    }

    public JSONArray getFullReviewsByTagNames(String[] tagNames) {
        JSONArray userReviewArray = new JSONArray();
        List<ReviewView> ls = reviewRepository.findDistinctByTagsNameIn(tagNames);
        for (ReviewView rv : ls) {
            JSONObject fr = null;
            try {
                fr = new JSONObject(objectMapper.writeValueAsString(new FullReview(rv)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            userReviewArray.put(fr);
        }

        return userReviewArray;
    }
}
