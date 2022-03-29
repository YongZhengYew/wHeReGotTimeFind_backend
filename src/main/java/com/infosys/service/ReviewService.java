package com.infosys.service;

import com.infosys.model.Review;
import com.infosys.model.projection.ReviewView;
import com.infosys.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService extends GenericModelService<Review, Integer, ReviewRepository> {
    public ReviewView getReviewView(Integer id) {
        return repository.findReviewViewById(id);
    }
}
